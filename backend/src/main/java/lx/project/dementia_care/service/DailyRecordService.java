package lx.project.dementia_care.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lx.project.dementia_care.dto.DailyRecordRequest;
import lx.project.dementia_care.dto.DailyRecordResponse;
import lx.project.dementia_care.dto.GeminiRequest;
import lx.project.dementia_care.dto.GeminiResponse;
import lx.project.dementia_care.dto.ReportDto;
import lx.project.dementia_care.entity.DailyRecord;
import lx.project.dementia_care.dto.UserDto;
import lx.project.dementia_care.mapper.DailyRecordMapper;
import lx.project.dementia_care.mapper.ReportMapper;
import lx.project.dementia_care.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class DailyRecordService {

    private final DailyRecordMapper recordMapper;
    private final UserMapper userMapper;
    private final GeminiService geminiService;
    private final ReportMapper reportMapper;

    public DailyRecordService(
            DailyRecordMapper recordMapper,
            UserMapper userMapper,
            GeminiService geminiService,
            ReportMapper reportMapper
    ) {
        this.recordMapper = recordMapper;
        this.userMapper = userMapper;
        this.geminiService = geminiService;
        this.reportMapper = reportMapper;
    }

    /**
     * 일별 기록 생성 또는 업데이트
     */
    public DailyRecordResponse saveOrUpdateRecord(DailyRecordRequest req) {
        Long uid = Long.valueOf(req.getUserId());
        UserDto user = userMapper.findById(uid)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        LocalDate date = LocalDate.parse(req.getRecordDate());

        Optional<DailyRecord> existing = recordMapper.findByUserIdAndDate(uid, date);
        DailyRecord record;
        if (existing.isPresent()) {
            record = existing.get();
            record.setContent(convertToJson(req));
            recordMapper.update(record);
        } else {
            record = new DailyRecord();
            record.setUserId(uid);
            record.setRecordDate(date);
            record.setContent(convertToJson(req));
            recordMapper.save(record);
        }

        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(record.getRecordId());
        res.setUserId(record.getUserId().toString());
        res.setRecordDate(record.getRecordDate());
        res.setContent(record.getContent());
        return res;
    }

    /**
     * 특정 사용자·날짜 일별 기록 조회
     */
    public DailyRecordResponse getRecord(String userIdString, String recordDateString) {
        Long userId = Long.valueOf(userIdString);
        LocalDate date = LocalDate.parse(recordDateString);

        DailyRecord record = recordMapper.findByUserIdAndDate(userId, date)
                 .orElseThrow(() -> new EntityNotFoundException("해당 날짜의 기록이 없습니다"));

        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(record.getRecordId());
        res.setUserId(record.getUserId().toString());
        res.setRecordDate(record.getRecordDate());
        res.setContent(record.getContent());
        return res;
    }

    /**
     * ID로 DailyRecord 조회
     */
    public DailyRecord getRecordById(Long id) {
        return recordMapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));
    }

    /**
     * Gemini API 호출 및 Report 저장
     */
    @Transactional
    public GeminiResponse analyzeAndSave(Long recordId) {
        DailyRecord record = getRecordById(recordId);

        GeminiRequest request = new GeminiRequest(
                record.getUserId().toString(),
                record.getContent()
        );
        GeminiResponse response = geminiService.analyzeRecord(request);

        ReportDto dto = new ReportDto(
                record.getPeriodId(),
                record.getUserId(),
                record.getContent(),
                response.getSummary(),
                response.getMetrics()
        );
        reportMapper.save(dto);

        return response;
    }

    // DailyRecordRequest → JSON 변환 헬퍼 메서드
    private String convertToJson(DailyRecordRequest req) {
        return "{\"mealAnswers\":" + req.getMealAnswers()
                + ",\"medicationAnswers\":" + req.getMedicationAnswers()
                + ",\"activityAnswers\":" + req.getActivityAnswers()
                + ",\"emotionAnswers\":" + req.getEmotionAnswers()
                + ",\"specialAnswers\":" + req.getSpecialAnswers() + "}";
    }
}

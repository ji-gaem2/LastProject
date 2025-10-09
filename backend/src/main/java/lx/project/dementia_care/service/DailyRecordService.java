package lx.project.dementia_care.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lx.project.dementia_care.dto.DailyRecordRequest;
import lx.project.dementia_care.dto.DailyRecordResponse;
import lx.project.dementia_care.dto.GeminiRequest;
import lx.project.dementia_care.dto.GeminiResponse;
import lx.project.dementia_care.entity.DailyRecord;
import lx.project.dementia_care.entity.Report;
import lx.project.dementia_care.entity.User;
import lx.project.dementia_care.repository.DailyRecordRepository;
import lx.project.dementia_care.repository.ReportRepository;
import lx.project.dementia_care.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * DailyRecord 관련 비즈니스 로직 처리 서비스
 */
@Service
@Slf4j
public class DailyRecordService {

    private final DailyRecordRepository recordRepo;
    private final UserRepository userRepo;
    private final GeminiService geminiService;         // GeminiService 주입
    private final ReportRepository reportRepository;    // ReportRepository 주입

    // 생성자에 새 리포지토리와 서비스 주입
    public DailyRecordService(DailyRecordRepository recordRepo,
                              UserRepository userRepo,
                              GeminiService geminiService,
                              ReportRepository reportRepository) {
        this.recordRepo = recordRepo;
        this.userRepo = userRepo;
        this.geminiService = geminiService;
        this.reportRepository = reportRepository;
    }

    /**
     * 일별 기록을 생성하거나 업데이트합니다.
     * 1) 요청 DTO에서 userId, recordDate, Map 응답값을 꺼냅니다.
     * 2) userRepo로 사용자 존재 여부 검증 후 조회합니다.
     * 3) recordRepo로 기존 레코드를 찾아, 없으면 새 인스턴스를 생성합니다.
     * 4) 조회·생성된 DailyRecord 엔티티에 값을 설정하고 저장합니다.
     * 5) 저장 결과를 응답 DTO로 변환해 반환합니다.
     *
     * @param req DailyRecordRequest 요청 DTO
     * @return DailyRecordResponse 응답 DTO
     */
    public DailyRecordResponse saveOrUpdateRecord(DailyRecordRequest req) {
        log.debug("▶▶▶ Received userId = {}", req.getUserId());
        Long uid = Long.valueOf(req.getUserId());
        log.debug("▶▶▶ Converted uid = {}", uid);

        User user = userRepo.findById(uid)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));
        log.debug("▶▶▶ Found user: {}", user);

        LocalDate date = LocalDate.parse(req.getRecordDate());

        DailyRecord record = recordRepo.findByUserUserIdAndRecordDate(uid, date)
                .orElse(new DailyRecord());

        record.setUser(user);
        record.setRecordDate(date);
        record.setMealAnswers(req.getMealAnswers());
        record.setMedicationAnswers(req.getMedicationAnswers());
        record.setActivityAnswers(req.getActivityAnswers());
        record.setEmotionAnswers(req.getEmotionAnswers());
        record.setSpecialAnswers(req.getSpecialAnswers());

        DailyRecord saved = recordRepo.save(record);
        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(saved.getId());
        res.setUserId(saved.getUser().getUserId().toString());
        res.setRecordDate(saved.getRecordDate());
        res.setMealAnswers(saved.getMealAnswers());
        res.setMedicationAnswers(saved.getMedicationAnswers());
        res.setActivityAnswers(saved.getActivityAnswers());
        res.setEmotionAnswers(saved.getEmotionAnswers());
        res.setSpecialAnswers(saved.getSpecialAnswers());
        return res;
    }

    /**
     * 특정 사용자와 날짜에 해당하는 일별 기록을 조회합니다.
     * 레코드가 없으면 EntityNotFoundException을 발생시킵니다.
     *
     * @param userIdString 사용자 ID 문자열
     * @param recordDateString 조회 날짜 문자열 (ISO 형식)
     * @return DailyRecordResponse 응답 DTO
     */
    public DailyRecordResponse getRecord(String userIdString, String recordDateString) {
        Long userId = Long.valueOf(userIdString);
        LocalDate date = LocalDate.parse(recordDateString);

        DailyRecord record = recordRepo.findByUserUserIdAndRecordDate(userId, date)
                .orElseThrow(() -> new EntityNotFoundException("해당 날짜의 기록이 없습니다"));

        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(record.getId());
        res.setUserId(record.getUser().getUserId().toString());
        res.setRecordDate(record.getRecordDate());
        res.setMealAnswers(record.getMealAnswers());
        res.setMedicationAnswers(record.getMedicationAnswers());
        res.setActivityAnswers(record.getActivityAnswers());
        res.setEmotionAnswers(record.getEmotionAnswers());
        res.setSpecialAnswers(record.getSpecialAnswers());
        return res;
    }

    /**
     * DB에서 ID로 DailyRecord를 조회합니다.
     */
    public DailyRecord getRecordById(Long id) {
        return recordRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + id));
    }

    /**
     * Gemini API를 호출하여 분석 후 Report로 저장합니다.
     */
@Transactional
public GeminiResponse analyzeAndSave(Long recordId) {
    // 1) DB에서 레코드 조회
    DailyRecord record = getRecordById(recordId);

    // 2) Gemini 요청 DTO 생성
    GeminiRequest request = new GeminiRequest(
            record.getUser().getUserId().toString(),
            record.getContent()
    );

    // 3) Gemini API 호출
    GeminiResponse response = geminiService.analyzeRecord(request);

    // 4) 결과를 Report 엔티티로 변환·저장
    Report report = new Report();
    report.setPeriod(record.getPeriod());              // DailyRecord가 포함한 Period 엔티티
    report.setPatient(record.getUser());               // DailyRecord가 포함한 User 엔티티
    report.setContent(record.getContent());            // 원본 텍스트
    report.setSummary(response.getSummary());          // 요약 텍스트
    report.setMetrics(response.getMetrics());          // 메트릭 데이터
    reportRepository.save(report);

    return response;
}
}

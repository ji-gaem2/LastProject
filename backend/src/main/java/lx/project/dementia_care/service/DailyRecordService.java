package lx.project.dementia_care.service;

import jakarta.persistence.EntityNotFoundException;
import lx.project.dementia_care.dto.DailyRecordRequest;
import lx.project.dementia_care.dto.DailyRecordResponse;
import lx.project.dementia_care.entity.DailyRecord;
import lx.project.dementia_care.entity.User;
import lx.project.dementia_care.repository.DailyRecordRepository;
import lx.project.dementia_care.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * DailyRecord 관련 비즈니스 로직 처리 서비스
 */
@Service
public class DailyRecordService {

    private final DailyRecordRepository recordRepo;
    private final UserRepository userRepo;

    public DailyRecordService(DailyRecordRepository recordRepo, UserRepository userRepo) {
        this.recordRepo = recordRepo;
        this.userRepo = userRepo;
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
        // 1. 요청에서 날짜 문자열을 LocalDate로 변환
        LocalDate date = LocalDate.parse(req.getRecordDate());

        // 2. 사용자 조회 (없으면 예외)
        User user = userRepo.findById(Long.valueOf(req.getUserId()))
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        // 3. 기존 레코드 조회 (존재하면 업데이트, 없으면 새로 생성)
        Optional<DailyRecord> optionalRecord =
            recordRepo.findByUserUserIdAndRecordDate(user.getUserId(), date);
        DailyRecord record = optionalRecord.orElse(new DailyRecord());

        // 4. 엔티티 필드 설정
        record.setUser(user);
        record.setRecordDate(date);
        record.setMealAnswers(req.getMealAnswers());
        record.setMedicationAnswers(req.getMedicationAnswers());
        record.setActivityAnswers(req.getActivityAnswers());
        record.setEmotionAnswers(req.getEmotionAnswers());
        record.setSpecialAnswers(req.getSpecialAnswers());

        // 5. 저장 및 응답 DTO 변환
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
}

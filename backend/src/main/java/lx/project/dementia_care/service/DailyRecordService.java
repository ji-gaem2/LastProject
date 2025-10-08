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

@Service
public class DailyRecordService {
    private final DailyRecordRepository repo;
    private final UserRepository userRepo;  // UserRepository 추가

    public DailyRecordService(DailyRecordRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public DailyRecordResponse saveOrUpdateRecord(DailyRecordRequest req) {
        LocalDate date = LocalDate.parse(req.getRecordDate());

        // User 객체 조회
        User user = userRepo.findById(Long.valueOf(req.getUserId()))
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        // 기존 레코드 조회 (메서드명 변경 필요: findByUserUserIdAndRecordDate)
        DailyRecord record = repo.findByUserUserIdAndRecordDate(user.getUserId(), date)
            .orElse(new DailyRecord());

        // User 객체 설정
        record.setUser(user);
        record.setRecordDate(date);
        record.setMealAnswers(req.getMealAnswers());
        record.setMedicationAnswers(req.getMedicationAnswers());
        record.setActivityAnswers(req.getActivityAnswers());
        record.setEmotionAnswers(req.getEmotionAnswers());
        record.setSpecialAnswers(req.getSpecialAnswers());

        DailyRecord saved = repo.save(record);

        // 응답 DTO 매핑
        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(saved.getId());
        res.setUserId(saved.getUser().getUserId().toString());
        // String → LocalDate 직접 설정 - 오류 해결
        res.setRecordDate(saved.getRecordDate());
        res.setMealAnswers(saved.getMealAnswers());
        res.setMedicationAnswers(saved.getMedicationAnswers());
        res.setActivityAnswers(saved.getActivityAnswers());
        res.setEmotionAnswers(saved.getEmotionAnswers());
        res.setSpecialAnswers(saved.getSpecialAnswers());
        return res;

    }

    public DailyRecordResponse getRecord(String userId, String recordDate) {
        Long uId = Long.valueOf(userId);
        LocalDate date = LocalDate.parse(recordDate);

        DailyRecord record = repo.findByUserUserIdAndRecordDate(uId, date)
            .orElseThrow(() -> new EntityNotFoundException("해당 날짜의 기록이 없습니다"));

        // getRecord 메서드도 동일하게 수정 - toString() 제거
        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(record.getId());
        res.setUserId(record.getUser().getUserId().toString());
        res.setRecordDate(record.getRecordDate());
        res.setMedicationAnswers(record.getMedicationAnswers());
        res.setActivityAnswers(record.getActivityAnswers());
        res.setEmotionAnswers(record.getEmotionAnswers());
        res.setSpecialAnswers(record.getSpecialAnswers());
        return res;

    }
}

package lx.project.dementia_care.service;

import jakarta.persistence.EntityNotFoundException;
import lx.project.dementia_care.dto.DailyRecordRequest;
import lx.project.dementia_care.dto.DailyRecordResponse;
import lx.project.dementia_care.entity.DailyRecord;
import lx.project.dementia_care.repository.DailyRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyRecordService {
    private final DailyRecordRepository repo;

    public DailyRecordService(DailyRecordRepository repo) {
        this.repo = repo;
    }

    public DailyRecordResponse saveOrUpdateRecord(DailyRecordRequest req) {
        LocalDate date = LocalDate.parse(req.getRecordDate());
        DailyRecord record = repo.findByUserIdAndRecordDate(req.getUserId(), date)
            .orElse(new DailyRecord());

        record.setUserId(req.getUserId());
        record.setRecordDate(date);
        record.setMealAnswers(req.getMealAnswers());
        record.setMedicationAnswers(req.getMedicationAnswers());
        record.setActivityAnswers(req.getActivityAnswers());
        record.setEmotionAnswers(req.getEmotionAnswers());
        record.setSpecialAnswers(req.getSpecialAnswers());

        DailyRecord saved = repo.save(record);

        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(saved.getId());
        res.setUserId(saved.getUserId());
        res.setRecordDate(saved.getRecordDate());
        res.setMealAnswers(saved.getMealAnswers());
        res.setMedicationAnswers(saved.getMedicationAnswers());
        res.setActivityAnswers(saved.getActivityAnswers());
        res.setEmotionAnswers(saved.getEmotionAnswers());
        res.setSpecialAnswers(saved.getSpecialAnswers());
        return res;
    }

    public DailyRecordResponse getRecord(String userId, String recordDate) {
        DailyRecord record = repo.findByUserIdAndRecordDate(userId, LocalDate.parse(recordDate))
            .orElseThrow(() -> new EntityNotFoundException("해당 날짜의 기록이 없습니다"));

        DailyRecordResponse res = new DailyRecordResponse();
        res.setId(record.getId());
        res.setUserId(record.getUserId());
        res.setRecordDate(record.getRecordDate());
        res.setMealAnswers(record.getMealAnswers());             // ← 직접 Map 사용
        res.setMedicationAnswers(record.getMedicationAnswers()); // ← 직접 Map 사용
        res.setActivityAnswers(record.getActivityAnswers());     // ← 직접 Map 사용
        res.setEmotionAnswers(record.getEmotionAnswers());       // ← 직접 Map 사용
        res.setSpecialAnswers(record.getSpecialAnswers());       // ← 직접 Map 사용
        return res;
    }
}

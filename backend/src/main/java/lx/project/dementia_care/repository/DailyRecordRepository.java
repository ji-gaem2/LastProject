package lx.project.dementia_care.repository;

import lx.project.dementia_care.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    Optional<DailyRecord> findByUserIdAndRecordDate(String userId, LocalDate recordDate);
}

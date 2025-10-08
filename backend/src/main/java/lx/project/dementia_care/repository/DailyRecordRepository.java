package lx.project.dementia_care.repository;

import lx.project.dementia_care.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * DailyRecord 엔티티용 JPA 리포지터리
 */
@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    /**
     * 특정 사용자(userId)와 날짜(recordDate)에 해당하는 DailyRecord 조회
     */
    Optional<DailyRecord> findByUserUserIdAndRecordDate(Long userId, LocalDate recordDate);

}

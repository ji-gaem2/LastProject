package lx.project.dementia_care.repository;

import lx.project.dementia_care.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * DailyRecord 엔티티에 대한 CRUD 기능 제공
 * - 사용자 ID와 날짜로 단건 조회하는 커스텀 메서드 포함
 */
@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    /**
     * 특정 사용자(userId)와 날짜(recordDate)에 해당하는 DailyRecord 조회
     * @param userId     User 엔티티의 PK
     * @param recordDate 조회할 날짜
     * @return DailyRecord Optional 감싸 반환
     */
    Optional<DailyRecord> findByUserUserIdAndRecordDate(Long userId, LocalDate recordDate);
}

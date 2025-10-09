package lx.project.dementia_care.repository;

import lx.project.dementia_care.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의
}

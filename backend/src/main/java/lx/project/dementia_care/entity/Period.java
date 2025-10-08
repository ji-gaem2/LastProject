package lx.project.dementia_care.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * 기간 엔티티 (Period)
 * - 특정 기간(시작/종료일) 정보를 저장
 */
@Entity
@Table(name = "\"Period\"", schema = "public")  // DB 테이블명이 Period
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_id")
    private Long periodId;  
    // PK: 기간 고유 ID

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;  
    // 시작 날짜

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;  
    // 종료 날짜

    // 기본 생성자 (JPA 요구)
    public Period() {}

    // 편의 생성자
    public Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter/Setter
    public Long getPeriodId() {
        return periodId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

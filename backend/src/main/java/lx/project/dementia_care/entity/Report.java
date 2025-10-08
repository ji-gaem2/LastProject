package lx.project.dementia_care.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 * 리포트 엔티티 (Report)
 * - DailyRecord 요약을 저장
 * - User와 다대일 관계 설정
 */
@Entity
@Table(name = "\"Report\"")  // DB 테이블명이 Report
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;  
    // PK: 리포트 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  
    // FK: User 엔티티와 다대일 관계

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;  
    // 요약 텍스트

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;  
    // 생성 시각

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;  
    // 수정 시각

    // 기본 생성자 (JPA 요구)
    public Report() {}

    // 편의 생성자
    public Report(User user, String summary) {
        this.user = user;
        this.summary = summary;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Getter/Setter
    public Long getReportId() {
        return reportId;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}

package lx.project.dementia_care.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 사용자 엔티티 (User)
 * - 치매 어르신 또는 보호자 정보를 담습니다.
 * - DailyRecord와 1:N 연관관계 설정합니다.
 */
@Entity
@Table(name = "\"User\"")  // 실제 DB 테이블명이 User이므로 큰따옴표 처리
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;  
    // PK: 사용자 고유 ID

    @Column(nullable = false, length = 100)
    private String name;  
    // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    private String email;  
    // 로그인 또는 식별용 이메일

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;  
    // 레코드 생성 시각

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;  
    // 레코드 수정 시각

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyRecord> records;  
    // DailyRecord와의 1:N 관계

    // 기본 생성자 (JPA 요구)
    public User() {}

    // 편의 생성자
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // 생성 전 호출: 생성·수정 시각 동시 설정
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    // 수정 전 호출: 수정 시각만 갱신
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Getter/Setter
    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<DailyRecord> getRecords() {
        return records;
    }
    public void setRecords(List<DailyRecord> records) {
        this.records = records;
    }
}

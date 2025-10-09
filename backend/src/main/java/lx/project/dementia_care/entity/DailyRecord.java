package lx.project.dementia_care.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import jakarta.persistence.*;

/**
 * 일별 기록 엔티티 (DailyRecord)
 * - 치매 어르신의 일일 설문 응답을 Map 형태로 저장
 * - 사용자(User)와 다대일 관계 설정
 * - 생성/수정 시각 자동 관리
 */
@Entity
@Table(name = "record")  // 기존 테이블명 Record를 그대로 사용
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")  
    private Long id;  // PK: 레코드 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  
    // FK: User 엔티티와 다대일 관계
    // user.getUserId() 컬럼에 매핑

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;
    // 설문 기록 날짜

    // 식사 관련 응답 저장용 테이블 및 컬럼 매핑
    @ElementCollection
    @CollectionTable(
        name = "record_meal_answers",            // Map을 저장할 별도 테이블명
        joinColumns = @JoinColumn(name = "record_id")  // 조인할 컬럼
    )
    @MapKeyColumn(name = "question_key")  // Map의 key를 저장할 컬럼명
    @Column(name = "answer_value")        // Map의 value를 저장할 컬럼명
    private Map<String, Integer> mealAnswers;  

    @ElementCollection
    @CollectionTable(
        name = "record_medication_answers",
        joinColumns = @JoinColumn(name = "record_id")
    )
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> medicationAnswers;  
    // 복약 관련 응답

    @ElementCollection
    @CollectionTable(
        name = "record_activity_answers",
        joinColumns = @JoinColumn(name = "record_id")
    )
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> activityAnswers;  
    // 활동 관련 응답

    @ElementCollection
    @CollectionTable(
        name = "record_emotion_answers",
        joinColumns = @JoinColumn(name = "record_id")
    )
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> emotionAnswers;  
    // 감정 관련 응답

    @ElementCollection
    @CollectionTable(
        name = "record_special_answers",
        joinColumns = @JoinColumn(name = "record_id")
    )
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> specialAnswers;  
    // 특이사항 관련 응답

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;  
    // 레코드 생성 시각, 최초 값만 설정

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;  
    // 레코드 최종 수정 시각

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    // 기본 생성자 (JPA 필수)
    public DailyRecord() {}

    // 편의 생성자
    public DailyRecord(User user,
                       LocalDate recordDate,
                       Map<String, Integer> mealAnswers,
                       Map<String, Integer> medicationAnswers,
                       Map<String, Integer> activityAnswers,
                       Map<String, Integer> emotionAnswers,
                       Map<String, Integer> specialAnswers) {
        this.user = user;
        this.recordDate = recordDate;
        this.mealAnswers = mealAnswers;
        this.medicationAnswers = medicationAnswers;
        this.activityAnswers = activityAnswers;
        this.emotionAnswers = emotionAnswers;
        this.specialAnswers = specialAnswers;
    }

    // 생성 전 호출: 생성 시각·수정 시각 동시 설정
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
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getRecordDate() { return this.recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public Map<String, Integer> getMealAnswers() { return mealAnswers; }
    public void setMealAnswers(Map<String, Integer> mealAnswers) { this.mealAnswers = mealAnswers; }

    public Map<String, Integer> getMedicationAnswers() { return medicationAnswers; }
    public void setMedicationAnswers(Map<String, Integer> medicationAnswers) { this.medicationAnswers = medicationAnswers; }

    public Map<String, Integer> getActivityAnswers() { return activityAnswers; }
    public void setActivityAnswers(Map<String, Integer> activityAnswers) { this.activityAnswers = activityAnswers; }

    public Map<String, Integer> getEmotionAnswers() { return emotionAnswers; }
    public void setEmotionAnswers(Map<String, Integer> emotionAnswers) { this.emotionAnswers = emotionAnswers; }

    public Map<String, Integer> getSpecialAnswers() { return specialAnswers; }
    public void setSpecialAnswers(Map<String, Integer> specialAnswers) { this.specialAnswers = specialAnswers; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // Lombok 또는 수동 게터/세터
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Period getPeriod() { return period; }
    public void setPeriod(Period period) { this.period = period; }

}

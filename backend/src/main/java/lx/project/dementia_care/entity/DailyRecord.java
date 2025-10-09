package lx.project.dementia_care.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import jakarta.persistence.*;

@Entity
@Table(name = "record")
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ElementCollection
    @CollectionTable(name = "record_meal_answers", joinColumns = @JoinColumn(name = "record_id"))
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> mealAnswers;

    @ElementCollection
    @CollectionTable(name = "record_medication_answers", joinColumns = @JoinColumn(name = "record_id"))
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> medicationAnswers;

    @ElementCollection
    @CollectionTable(name = "record_activity_answers", joinColumns = @JoinColumn(name = "record_id"))
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> activityAnswers;

    @ElementCollection
    @CollectionTable(name = "record_emotion_answers", joinColumns = @JoinColumn(name = "record_id"))
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> emotionAnswers;

    @ElementCollection
    @CollectionTable(name = "record_special_answers", joinColumns = @JoinColumn(name = "record_id"))
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer_value")
    private Map<String, Integer> specialAnswers;

    @Column(name = "period_id", nullable = false)
    private Long periodId;
    public Long getPeriodId() { return periodId; }
    public void setPeriodId(Long periodId) { this.periodId = periodId; }

    public Long getRecordId() {
        return recordId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Map<String, Integer> getMealAnswers() {
        return mealAnswers;
    }
    public void setMealAnswers(Map<String, Integer> mealAnswers) {
        this.mealAnswers = mealAnswers;
    }

    public Map<String, Integer> getMedicationAnswers() {
        return medicationAnswers;
    }
    public void setMedicationAnswers(Map<String, Integer> medicationAnswers) {
        this.medicationAnswers = medicationAnswers;
    }

    public Map<String, Integer> getActivityAnswers() {
        return activityAnswers;
    }
    public void setActivityAnswers(Map<String, Integer> activityAnswers) {
        this.activityAnswers = activityAnswers;
    }

    public Map<String, Integer> getEmotionAnswers() {
        return emotionAnswers;
    }
    public void setEmotionAnswers(Map<String, Integer> emotionAnswers) {
        this.emotionAnswers = emotionAnswers;
    }

    public Map<String, Integer> getSpecialAnswers() {
        return specialAnswers;
    }
    public void setSpecialAnswers(Map<String, Integer> specialAnswers) {
        this.specialAnswers = specialAnswers;
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
}

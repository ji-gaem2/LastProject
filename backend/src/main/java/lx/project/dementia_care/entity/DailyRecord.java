package lx.project.dementia_care.entity;

import java.time.LocalDate;
import java.util.Map;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"Record\"")
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;                     // ← 추가
    private LocalDate recordDate;              // ← 추가

    @ElementCollection
    private Map<String, Integer> mealAnswers;       // ← Map으로 변경
    @ElementCollection
    private Map<String, Integer> medicationAnswers; // ← Map으로 변경
    @ElementCollection
    private Map<String, Integer> activityAnswers;   // ← Map으로 변경
    @ElementCollection
    private Map<String, Integer> emotionAnswers;    // ← Map으로 변경
    @ElementCollection
    private Map<String, Integer> specialAnswers;    // ← Map으로 변경

    // 기본 생성자
    public DailyRecord() {}

    // 생성자
    public DailyRecord(String userId, LocalDate recordDate,
                       Map<String, Integer> mealAnswers,
                       Map<String, Integer> medicationAnswers,
                       Map<String, Integer> activityAnswers,
                       Map<String, Integer> emotionAnswers,
                       Map<String, Integer> specialAnswers) {
        this.userId = userId;
        this.recordDate = recordDate;
        this.mealAnswers = mealAnswers;
        this.medicationAnswers = medicationAnswers;
        this.activityAnswers = activityAnswers;
        this.emotionAnswers = emotionAnswers;
        this.specialAnswers = specialAnswers;
    }

    // Getter/Setter
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDate getRecordDate() { return recordDate; }
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
}

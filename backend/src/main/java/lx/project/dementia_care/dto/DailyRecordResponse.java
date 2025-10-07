// src/main/java/lx/project/dementia_care/dto/DailyRecordResponse.java
package lx.project.dementia_care.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class DailyRecordResponse {
    private Long id;
    private String userId;
    private LocalDate recordDate;               // ← LocalDate
    private Map<String,Integer> mealAnswers;
    private Map<String,Integer> medicationAnswers;
    private Map<String,Integer> activityAnswers;
    private Map<String,Integer> emotionAnswers;
    private Map<String,Integer> specialAnswers;
}

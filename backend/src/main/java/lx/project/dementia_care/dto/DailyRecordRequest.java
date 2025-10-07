package lx.project.dementia_care.dto;

import lombok.Data;
import java.util.Map;

@Data
public class DailyRecordRequest {
    private String userId;
    private String recordDate;
    private Map<String, Integer> mealAnswers;
    private Map<String, Integer> medicationAnswers;
    private Map<String, Integer> activityAnswers;
    private Map<String, Integer> emotionAnswers;
    private Map<String, Integer> specialAnswers;
}

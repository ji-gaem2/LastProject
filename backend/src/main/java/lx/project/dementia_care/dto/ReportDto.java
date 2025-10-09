package lx.project.dementia_care.dto;

import java.util.Map;

public class ReportDto {
    private Long periodId;
    private Long patientId;
    private String content;
    private String summary;
    private Map<String, Object> metrics;

    public ReportDto(Long periodId, Long patientId,
                     String content, String summary,
                     Map<String, Object> metrics) {
        this.periodId = periodId;
        this.patientId = patientId;
        this.content = content;
        this.summary = summary;
        this.metrics = metrics;
    }

    // getters
    public Long getPeriodId() { return periodId; }
    public Long getPatientId() { return patientId; }
    public String getContent() { return content; }
    public String getSummary() { return summary; }
    public Map<String, Object> getMetrics() { return metrics; }
}

package lx.project.dementia_care.dto;

import java.util.Map;

public class GeminiResponse {
    private String summary;
    private Map<String, Object> metrics;

    public GeminiResponse() {}

    public GeminiResponse(String summary, Map<String, Object> metrics) {
        this.summary = summary;
        this.metrics = metrics;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
    }
}

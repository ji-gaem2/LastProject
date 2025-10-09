package lx.project.dementia_care.dto;

public class GeminiRequest {
    private String userId;
    private String recordText;

    public GeminiRequest() {}

    public GeminiRequest(String userId, String recordText) {
        this.userId = userId;
        this.recordText = recordText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecordText() {
        return recordText;
    }

    public void setRecordText(String recordText) {
        this.recordText = recordText;
    }
}

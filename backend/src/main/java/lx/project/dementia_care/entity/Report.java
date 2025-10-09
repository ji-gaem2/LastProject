package lx.project.dementia_care.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lx.project.dementia_care.entity.Period;
import lx.project.dementia_care.entity.User;


import java.time.OffsetDateTime;
import java.util.Map;

/**
 * 리포트 엔티티 (Report)
 */
@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Report {

    /** 리포트 고유 ID (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    /** 리포트 대상 기간 (Period 엔티티와 다대일) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    /** 리포트 대상 환자 (User 엔티티와 다대일) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    /** 원본 기록 텍스트 (DailyRecord.content) */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** Gemini API가 생성한 요약 텍스트 */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String summary;

    /** 분석 메트릭(통계 등)을 JSON 형태로 저장(jsonb 컬럼) */
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metrics;

    /** 생성 시각 (자동 설정) */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    /** 수정 시각 (자동 업데이트) */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    /**
     * 편의 생성자
     *
     * @param period   기간
     * @param patient  분석 대상
     * @param content  원본 기록
     * @param summary  요약
     * @param metrics  메트릭(분석)
     */
    public Report(Period period,
                  User patient,
                  String content,
                  String summary,
                  Map<String, Object> metrics) {
        this.period = period;
        this.patient = patient;
        this.content = content;
        this.summary = summary;
        this.metrics = metrics;
    }
}

package lx.project.dementia_care.controller;

import lx.project.dementia_care.dto.DailyRecordRequest;
import lx.project.dementia_care.dto.DailyRecordResponse;
import lx.project.dementia_care.service.DailyRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 일별 기록
 */
@RestController
@RequestMapping("/api/record")
public class DailyRecordController {

    private final DailyRecordService service;

    public DailyRecordController(DailyRecordService service) {
        this.service = service;
    }

    /**
     * 일별 기록 생성 또는 업데이트
     * @param req DailyRecordRequest 요청 DTO
     * @return DailyRecordResponse 응답 DTO
     */
    @PostMapping
    public ResponseEntity<DailyRecordResponse> saveOrUpdate(
            @RequestBody DailyRecordRequest req) {
        DailyRecordResponse res = service.saveOrUpdateRecord(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /**
     * 특정 사용자와 날짜의 기록 조회
     * @param userId 사용자 ID
     * @param date   조회 날짜 (ISO yyyy-MM-dd)
     * @return DailyRecordResponse 응답 DTO
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<DailyRecordResponse> getByUserAndDate(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyRecordResponse res = service.getRecord(userId, date.toString());
        return ResponseEntity.ok(res);
    }
}

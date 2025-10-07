package lx.project.dementia_care.controller;

import lx.project.dementia_care.dto.DailyRecordRequest;
import lx.project.dementia_care.dto.DailyRecordResponse;
import lx.project.dementia_care.service.DailyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/daily-records")
@RequiredArgsConstructor
public class DailyRecordController {

    private final DailyRecordService recordService;

    // 기록 저장 또는 수정 API
    @PostMapping
    public ResponseEntity<DailyRecordResponse> saveRecord(@RequestBody DailyRecordRequest request) {
        DailyRecordResponse response = recordService.saveOrUpdateRecord(request);
        return ResponseEntity.ok(response);
    }

    // 특정 사용자, 날짜 기록 조회
    @GetMapping("/{userId}/{recordDate}")
    public ResponseEntity<DailyRecordResponse> getRecord(@PathVariable String userId,
                                                    @PathVariable String recordDate) {
        DailyRecordResponse response = recordService.getRecord(userId, recordDate);
        return ResponseEntity.ok(response);
    }
}

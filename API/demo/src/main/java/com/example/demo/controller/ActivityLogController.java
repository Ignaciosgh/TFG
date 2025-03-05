package com.example.demo.controller;

import com.example.demo.model.ActivityLogs;
import com.example.demo.service.ActivityLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public List<ActivityLogs> getAllActivityLogs() {
        return activityLogService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityLogs> getActivityLogById(@PathVariable Long id) {
        ActivityLogs activityLog = activityLogService.findById(id);
        return activityLog != null ? ResponseEntity.ok(activityLog) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ActivityLogs createActivityLog(@RequestBody ActivityLogs activityLog) {
        return activityLogService.save(activityLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityLogs> updateActivityLog(@PathVariable Long id, @RequestBody ActivityLogs activityLogDetails) {
        ActivityLogs updatedActivityLog = activityLogService.update(id, activityLogDetails);
        return updatedActivityLog != null ? ResponseEntity.ok(updatedActivityLog) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityLog(@PathVariable Long id) {
        boolean deleted = activityLogService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
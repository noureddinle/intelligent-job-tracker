package com.jobtracker.controller;

import com.jobtracker.dto.job.JobRequest;
import com.jobtracker.dto.job.JobResponse;
import com.jobtracker.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<JobResponse> createJob(@PathVariable Long userId, @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.createJob(userId, request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobResponse>> getUserJobs(@PathVariable Long userId) {
        return ResponseEntity.ok(jobService.getJobs(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchJobs(@RequestParam Map<String, String> body) {
        String query = body.get("query");
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Double threshold = Double.parseDouble(body.get("threshold"));
        if (threshold == null || threshold < 0 || threshold > 1) {
            return ResponseEntity.badRequest().build();
        }
        Integer limit = Integer.parseInt(body.get("limit"));
        if (limit == null || limit <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(jobService.searchSimilarJobs(query, threshold, limit));
    }
}
package com.jobtracker.controller;

import com.jobtracker.dto.job.JobRequest;
import com.jobtracker.dto.job.JobResponse;
import com.jobtracker.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

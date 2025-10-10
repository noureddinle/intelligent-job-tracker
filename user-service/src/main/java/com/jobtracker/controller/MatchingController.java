package com.jobtracker.controller;

import com.jobtracker.service.MatchingService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/")
    public ResponseEntity<String> match() {
        return ResponseEntity.ok("Matching API");
    }

    @GetMapping("/resume-to-jobs/{resumeId}")
    public ResponseEntity<List<Map<String, Object>>> matchResumeToJobs(@PathVariable Long resumeId, @RequestParam(defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(matchingService.matchResumeToJobs(resumeId, limit));
    }

    @GetMapping("/jobs-to-resume/{jobId}")
    public ResponseEntity<List<Map<String, Object>>> matchJobsToResume(@PathVariable Long jobId, @RequestParam(defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(matchingService.matchJobsToResume(jobId, limit));
    }
}

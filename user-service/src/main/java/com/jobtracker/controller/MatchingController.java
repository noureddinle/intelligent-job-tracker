package com.jobtracker.controller;

import com.jobtracker.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/resume-to-jobs/{resumeId}")
    public List<Map<String, Object>> matchResumeToJobs(@PathVariable Long resumeId) {
        return matchingService.matchResumeToJobs(resumeId);
    }

    @GetMapping("/jobs-to-resume/{jobId}")
    public List<Map<String, Object>> matchJobsToResume(@PathVariable Long jobId) {
        return matchingService.matchJobsToResume(jobId);
    }
}

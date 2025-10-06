package com.jobtracker.controller;

import com.jobtracker.dto.resume.ResumeResponse;
import com.jobtracker.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getAllResumes() {
        List<ResumeResponse> resumes = resumeService.getAllResumes();
        return ResponseEntity.ok(resumes);
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<ResumeResponse> uploadResume(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(resumeService.uploadResume(userId, file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResume(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.getResume(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResumeResponse>> getUserResumes(@PathVariable Long userId) {
        return ResponseEntity.ok(resumeService.getUserResumes(userId));
    }

    @GetMapping("/{id}/parsed")
    public ResponseEntity<String> getResumeParsedText(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.getResumeParsedText(id));
    }

    @PostMapping("/search")
    public List<Map<String, Object>> searchResumes(@RequestParam Map<String, String> body) {
        String queryText = body.get("query");
        return resumeService.searchSimilarResumes(queryText);
    }
}

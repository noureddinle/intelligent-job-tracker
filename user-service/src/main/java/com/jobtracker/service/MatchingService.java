package com.jobtracker.service;

import com.jobtracker.model.Resume;
import com.jobtracker.repository.JobRepository;
import com.jobtracker.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;

    public List<Map<String, Object>> matchResumeToJobs(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (resume.getEmbedding() == null) {
            throw new RuntimeException("Resume does not have an embedding");
        }

        // Convert embedding array to PostgreSQL vector format
        String embeddingString = Arrays.toString(resume.getEmbedding())
                .replace("[", "(")
                .replace("]", ")");

        List<Object[]> results = jobRepository.findJobsByResumeEmbedding(embeddingString);

        List<Map<String, Object>> matches = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> job = new HashMap<>();
            job.put("id", row[0]);
            job.put("title", row[1]);
            job.put("description", row[2]);
            job.put("similarity", row[3]);
            matches.add(job);
        }

        return matches;
    }

    public List<Map<String, Object>> matchJobsToResume(Long jobId) {
        var job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getEmbedding() == null) {
            throw new RuntimeException("Job does not have an embedding");
        }

        // Convert embedding array to PostgreSQL vector format
        String embeddingString = Arrays.toString(job.getEmbedding())
                .replace("[", "(")
                .replace("]", ")");

        List<Object[]> results = resumeRepository.findResumesByJobEmbedding(embeddingString);

        List<Map<String, Object>> matches = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> resume = new HashMap<>();
            resume.put("id", row[0]);
            resume.put("file_name", row[1]);
            resume.put("file_url", row[2]);
            resume.put("similarity", row[3]);
            matches.add(resume);
        }

        return matches;
    }
}

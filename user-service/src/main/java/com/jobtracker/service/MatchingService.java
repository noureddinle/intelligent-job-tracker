package com.jobtracker.service;

import com.jobtracker.model.Resume;
import com.jobtracker.model.Job;
import com.jobtracker.repository.JobRepository;
import com.jobtracker.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;

    public List<Map<String, Object>> matchResumeToJobs(Long resumeId, Integer limit) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        if (resume.getEmbedding() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resume does not have an embedding");
        }

        String embeddingString = "[" + IntStream.range(0, resume.getEmbedding().length)
            .mapToObj(i -> Float.toString(resume.getEmbedding()[i]))
            .collect(Collectors.joining(", ")) + "]";

        List<Object[]> results = jobRepository.findJobsByResumeEmbedding(embeddingString, limit);

        List<Map<String, Object>> matches = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> job = new HashMap<>();
            job.put("id", row[0]);
            job.put("title", row[1]);
            job.put("company", row[2]);
            job.put("description", row[3]);
            job.put("similarity", row[4]);
            job.put("location", row[5]);
            job.put("salary", row[6]);
            job.put("created_at", row[7]);
            job.put("updated_at", row[8]);
            matches.add(job);
        }

        return matches.stream()
                .filter(job -> ((Double) job.get("similarity")) > 0.75)
                .sorted((a, b) -> Double.compare((Double) b.get("similarity"), (Double) a.get("similarity")))
                .toList();
    }

    public List<Map<String, Object>> matchJobsToResume(Long jobId, Integer limit) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        if (job.getEmbedding() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job does not have an embedding");
        }

        String embeddingString = "[" + IntStream.range(0, job.getEmbedding().length)
            .mapToObj(i -> Float.toString(job.getEmbedding()[i]))
            .collect(Collectors.joining(", ")) + "]";

        List<Object[]> results = resumeRepository.findResumesByJobEmbedding(embeddingString, limit);

        List<Map<String, Object>> matches = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> resume = new HashMap<>();
            resume.put("id", row[0]);
            resume.put("user_id", row[1]);
            resume.put("file_name", row[2]);
            resume.put("file_type", row[3]);
            resume.put("user_name", row[4]);
            resume.put("file_url", row[5]);
            resume.put("similarity", row[6]);
            resume.put("created_at", row[7]);
            resume.put("updated_at", row[8]);
            matches.add(resume);
        }

        return matches.stream()
                .filter(resume -> ((Double) resume.get("similarity")) > 0.75)
                .sorted((a, b) -> Double.compare((Double) b.get("similarity"), (Double) a.get("similarity")))
                .toList();
    }
}

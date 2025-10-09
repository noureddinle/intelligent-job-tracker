package com.jobtracker.service;

import com.jobtracker.dto.job.JobRequest;
import com.jobtracker.dto.job.JobResponse;
import com.jobtracker.model.Job;
import com.jobtracker.model.User;
import com.jobtracker.repository.JobRepository;
import com.jobtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final EmbeddingService embeddingService;

    public JobResponse createJob(Long userId, JobRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setStatus(request.getStatus());
        job.setUser(user);

        String description = request.getDescription() != null ? request.getDescription() : "";
        float[] embedding = embeddingService.getEmbedding(description);
        job.setEmbedding(embedding);

        Job savedJob = jobRepository.save(job);
        return mapToResponse(savedJob);
    }

    public List<JobResponse> getJobs(Long userId) {
        return jobRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(Long id) {
        return jobRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
        jobRepository.deleteById(id);
    }

    public List<Map<String, Object>> searchSimilarJobs(String queryText) {
        float[] queryEmbedding = embeddingService.getEmbedding(queryText);
        String embeddingString = Arrays.toString(queryEmbedding);
        List<Object[]> results = jobRepository.findSimilarJobs(embeddingString);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("title", row[1]);
            map.put("description", row[2]);
            map.put("similarity", row[3]);
            list.add(map);
        }
        return list;
    }

    private JobResponse mapToResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getStatus(),
                job.getAppliedDate(),
                job.getUser().getId()
        );
    }
}
package com.jobtracker.service;

import com.jobtracker.dto.resume.ResumeResponse;
import com.jobtracker.model.Resume;
import com.jobtracker.model.User;
import com.jobtracker.repository.ResumeRepository;
import com.jobtracker.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final EmbeddingService embeddingService;
    private final Tika tika = new Tika();


    private ResumeResponse mapToResponse(Resume resume) {
        int embeddingLength = resume.getEmbedding() != null ? resume.getEmbedding().length : 0;
        return new ResumeResponse(resume.getId(), resume.getFileUrl(), resume.getUser().getId(), "Resume uploaded successfully", "Embedding: " + embeddingLength);
    }

    public ResumeResponse uploadResume(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();
        String fileUrl = supabaseStorageService.uploadFile(fileName, fileBytes);

        String extractedText;
        try {
            extractedText = tika.parseToString(file.getInputStream());
        } catch (IOException | TikaException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to extract text from resume" + e.getMessage());
        }

        float[] embedding = embeddingService.getEmbedding(extractedText);

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileUrl(fileUrl);
        resume.setFileType(file.getContentType());
        resume.setFileName(file.getOriginalFilename());
        resume.setParsedText(extractedText);
        resume.setEmbedding(embedding);
        Resume savedResume = resumeRepository.save(resume);
        return mapToResponse(savedResume);
    }

    public List<Map<String, Object>> searchSimilarResumes(String queryText) {
        float[] queryEmbedding = embeddingService.getEmbedding(queryText);
        String embeddingString = Arrays.toString(queryEmbedding)
        .replace("[", "(").replace("]", ")");

        List<Object[]> results = resumeRepository.findSimilarResumes(embeddingString);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("file_name", row[1]);
            map.put("file_url", row[2]);
            map.put("similarity", row[3]);
            list.add(map);
        }
        return list;
    }

    public List<ResumeResponse> getUserResumes(Long userId) {
        return resumeRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ResumeResponse getResume(Long id) {
        return resumeRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> getAllResumes() {
        return resumeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Resume getResumeEntity(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
    }

    public String getResumeParsedText(Long id) {
        Resume resume = getResumeEntity(id);
        return resume.getParsedText();
    }
}

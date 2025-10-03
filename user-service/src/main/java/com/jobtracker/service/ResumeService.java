package com.jobtracker.service;

import com.jobtracker.dto.ResumeResponse;
import com.jobtracker.model.Resume;
import com.jobtracker.model.User;
import com.jobtracker.repository.ResumeRepository;
import com.jobtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final SupabaseStorageService supabaseStorageService;

    private ResumeResponse mapToResponse(Resume resume) {
        return new ResumeResponse(resume.getId(), resume.getFileUrl(), resume.getUser().getId());
    }

    public ResumeResponse uploadResume(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();
        String fileUrl = supabaseStorageService.uploadFile(fileName, fileBytes);

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileUrl(fileUrl);
        
        return mapToResponse(resumeRepository.save(resume));
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
                
}

package com.jobtracker.repository;

import com.jobtracker.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserId(Long userId);
    List<Resume> findByParsedTextContainingIgnoreCase(String keyword);
    List<Resume> findByParsedTextEmbeddingSimilar(float[] embedding, int topK);
    Resume deleteResumeById(Long id);
}


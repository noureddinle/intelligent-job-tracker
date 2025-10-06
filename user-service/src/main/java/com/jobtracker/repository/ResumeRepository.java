package com.jobtracker.repository;

import com.jobtracker.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserId(Long userId);
    List<Resume> findByParsedTextContainingIgnoreCase(String keyword);
    @Query(value = "SELECT * FROM resumes ORDER BY embedding <-> :embedding LIMIT :topK", nativeQuery = true)
    List<Resume> findByEmbeddingSimilar(@Param("embedding") float[] embedding, @Param("topK") int topK);
    Resume deleteResumeById(Long id);
}


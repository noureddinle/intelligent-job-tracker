package com.jobtracker.repository;

import com.jobtracker.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    // 🔍 Search resumes semantically similar to a given query text
    @Query(value = """
        SELECT 
            id, 
            file_name, 
            file_url, 
            1 - (embedding <=> CAST(:queryEmbedding AS vector)) AS similarity
        FROM resumes
        WHERE embedding IS NOT NULL
        ORDER BY embedding <=> CAST(:queryEmbedding AS vector)
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findSimilarResumes(@Param("queryEmbedding") String queryEmbedding);

    // 🤝 Find top resumes similar to a specific job’s embedding
    @Query(value = """
        SELECT 
            id, 
            user_id, 
            file_name, 
            file_url, 
            1 - (embedding <=> CAST(:jobEmbedding AS vector)) AS similarity
        FROM resumes
        WHERE embedding IS NOT NULL
        ORDER BY embedding <=> CAST(:jobEmbedding AS vector)
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findResumesByJobEmbedding(@Param("jobEmbedding") String jobEmbedding);

    List<Resume> findByUserId(Long userId);
    List<Resume> findByParsedTextContainingIgnoreCase(String keyword);
    Resume deleteResumeById(Long id);
}

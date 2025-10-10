package com.jobtracker.repository;

import com.jobtracker.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    @Query(value = """
        SELECT 
            r.id, 
            r.file_name, 
            r.file_url, 
            1 - (r.embedding <=> CAST(:queryEmbedding AS vector)) AS similarity
        FROM resumes r
        WHERE r.embedding IS NOT NULL
        ORDER BY r.embedding <=> CAST(:queryEmbedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findSimilarResumes(@Param("queryEmbedding") String queryEmbedding, @Param("limit") Integer limit);
    @Query(value = """
        SELECT 
            r.id, 
            r.user_id, 
            r.file_type,
            r.file_name, 
            r.file_url, 
            1 - (r.embedding <=> CAST(:jobEmbedding AS vector)) AS similarity
        FROM resumes r
        WHERE r.embedding IS NOT NULL
        ORDER BY r.embedding <=> CAST(:jobEmbedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findResumesByJobEmbedding(@Param("jobEmbedding") String jobEmbedding, @Param("limit") Integer limit);
    List<Resume> findByUserId(Long userId);
    List<Resume> findByParsedTextContainingIgnoreCase(String keyword);
}
package com.jobtracker.repository;

import com.jobtracker.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // 🔹 Fetch all jobs for a given user
    List<Job> findByUserId(Long userId);

    // 🔹 Find top 5 jobs similar to a text query embedding
    @Query(value = """
        SELECT 
            id, 
            title, 
            description, 
            1 - (embedding <=> CAST(:queryEmbedding AS vector)) AS similarity
        FROM jobs
        WHERE embedding IS NOT NULL
        ORDER BY embedding <=> CAST(:queryEmbedding AS vector)
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findSimilarJobs(@Param("queryEmbedding") String queryEmbedding);

    // 🔹 Find top 5 jobs most similar to a resume embedding
    @Query(value = """
        SELECT 
            id, 
            title, 
            description, 
            1 - (embedding <=> CAST(:resumeEmbedding AS vector)) AS similarity
        FROM jobs
        WHERE embedding IS NOT NULL
        ORDER BY embedding <=> CAST(:resumeEmbedding AS vector)
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findJobsByResumeEmbedding(@Param("resumeEmbedding") String resumeEmbedding);
}

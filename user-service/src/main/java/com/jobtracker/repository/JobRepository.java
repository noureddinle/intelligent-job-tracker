package com.jobtracker.repository;

import com.jobtracker.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("SELECT j FROM Job j WHERE j.user.id = :userId")
    List<Job> findByUserId(Long userId);

    @Query(value = """
        SELECT 
            j.id, 
            j.title, 
            j.company,
            j.location,
            j.description,
            1 - (embedding <=> CAST(:queryEmbedding AS vector)) AS similarity
        FROM jobs j
        WHERE j.embedding IS NOT NULL
        ORDER BY j.embedding <=> CAST(:queryEmbedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findSimilarJobs(@Param("queryEmbedding") String queryEmbedding, @Param("limit") Integer limit);

    @Query(value = """
        SELECT 
            j.id, 
            j.title, 
            j.company,
            j.location,
            j.description,
            1 - (j.embedding <=> CAST(:resumeEmbedding AS vector)) AS similarity
        FROM jobs j
        WHERE j.embedding IS NOT NULL
        ORDER BY j.embedding <=> CAST(:resumeEmbedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findJobsByResumeEmbedding(@Param("resumeEmbedding") String resumeEmbedding, @Param("limit") Integer limit);
}
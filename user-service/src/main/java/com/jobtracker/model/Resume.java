package com.jobtracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;
    private String fileType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String parsedText;

    // 🧠 Embedding vector for semantic similarity
    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    // Store raw binary (if you want to allow downloads)
    @Lob
    private byte[] data;

    // Extracted job-related metadata
    private String jobTitle;
    private String jobLocation;
    private String jobDescription;
    private String jobCompany;
    private String jobUrl;
    private String jobSalary;

    // Link to the user who uploaded the resume
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    // Optional: cleaner toString() for debugging
    @Override
    public String toString() {
        return "Resume{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", userId=" + (user != null ? user.getId() : null) +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}

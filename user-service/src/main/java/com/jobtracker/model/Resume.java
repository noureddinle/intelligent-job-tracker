package com.jobtracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "resumes")
@Data
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;
    private String fileType;
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    @Lob
    @Column(columnDefinition = "TEXT")
    private String parsedText;

    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    @Lob
    private byte[] data; 
    private String jobTitle;
    private String jobLocation;
    private String jobDescription;
    private String jobCompany;
    private String jobUrl;
    private String jobSalary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }
}

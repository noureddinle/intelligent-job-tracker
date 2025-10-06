package com.jobtracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String company;
    private String location;
    private String status; 

    private LocalDateTime appliedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    @PrePersist
    protected void onCreate() {
        appliedDate = LocalDateTime.now();
        if (status == null) status = "Applied";
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public float[] getEmbedding() {
        return embedding;
    }

}

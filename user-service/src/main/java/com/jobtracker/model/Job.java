package com.jobtracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String company;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String jobUrl;
    private String salary;
    private String status;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🧠 Embedding vector for semantic search
    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    // Automatically set defaults before insertion
    @PrePersist
    protected void onCreate() {
        appliedDate = LocalDateTime.now();
        if (status == null) status = "Open";
    }

    // Optional: readable toString without embedding noise
    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", appliedDate=" + appliedDate +
                '}';
    }
}

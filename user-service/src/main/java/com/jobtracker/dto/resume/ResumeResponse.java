package com.jobtracker.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumeResponse {
    private Long id;
    private String fileUrl;
    private Long userId;
}

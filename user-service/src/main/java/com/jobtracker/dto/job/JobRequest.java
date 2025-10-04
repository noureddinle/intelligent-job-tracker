package com.jobtracker.dto.job;

import lombok.Data;

@Data
public class JobRequest {
    private String title;
    private String company;
    private String location;
    private String status;
}

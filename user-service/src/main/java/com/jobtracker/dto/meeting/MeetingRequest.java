package com.jobtracker.dto.meeting;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingRequest {
    private String topic;
    private Long recruiterId;
    private Long CondidateId;
    private LocalDateTime startTime;
}

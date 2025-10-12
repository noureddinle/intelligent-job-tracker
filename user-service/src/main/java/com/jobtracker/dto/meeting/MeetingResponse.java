package com.jobtracker.dto.meeting;

import com.jobtracker.Enum.MeetingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MeetingResponse {
    private Long id;
    private String roomId;
    private String topic;
    private Long recruiterId;
    private Long CondidateId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private MeetingStatus status;
}

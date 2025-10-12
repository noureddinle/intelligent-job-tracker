package com.jobtracker.service;

import com.jobtracker.dto.meeting.MeetingRequest;
import com.jobtracker.dto.meeting.MeetingResponse;
import com.jobtracker.model.Meeting;
import com.jobtracker.Enum.MeetingStatus;
import com.jobtracker.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public MeetingResponse scheduleMeeting(MeetingRequest request) {
        Meeting meeting = Meeting.builder()
                .topic(request.getTopic())
                .recruiterId(request.getRecruiterId())
                .CondidateId(request.getCondidateId())
                .startTime(request.getStartTime())
                .status(MeetingStatus.SCHEDULED)
                .roomId(UUID.randomUUID().toString())
                .build();

        Meeting saved = meetingRepository.save(meeting);
        return mapToResponse(saved);
    }

    public MeetingResponse getMeetingByRoomId(String roomId) {
        Meeting meeting = meetingRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));
        return mapToResponse(meeting);
    }

    public List<MeetingResponse> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MeetingResponse updateStatus(Long id, MeetingStatus status) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting not found"));
        meeting.setStatus(status);

        if (status == MeetingStatus.COMPLETED || status == MeetingStatus.CANCELLED) {
            meeting.setEndTime(LocalDateTime.now());
        }

        return mapToResponse(meetingRepository.save(meeting));
    }

    private MeetingResponse mapToResponse(Meeting meeting) {
        return MeetingResponse.builder()
                .id(meeting.getId())
                .roomId(meeting.getRoomId())
                .topic(meeting.getTopic())
                .recruiterId(meeting.getRecruiterId())
                .CondidateId(meeting.getCondidateId())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .status(meeting.getStatus())
                .build();
    }
}

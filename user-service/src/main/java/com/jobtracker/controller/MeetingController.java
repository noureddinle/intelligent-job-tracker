package com.jobtracker.controller;

import com.jobtracker.dto.meeting.MeetingRequest;
import com.jobtracker.dto.meeting.MeetingResponse;
import com.jobtracker.Enum.MeetingStatus;
import com.jobtracker.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingResponse> scheduleMeeting(@RequestBody MeetingRequest request) {
        return ResponseEntity.ok(meetingService.scheduleMeeting(request));
    }

    @GetMapping
    public ResponseEntity<List<MeetingResponse>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<MeetingResponse> getMeetingByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(meetingService.getMeetingByRoomId(roomId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MeetingResponse> updateMeetingStatus(
            @PathVariable Long id,
            @RequestParam MeetingStatus status
    ) {
        return ResponseEntity.ok(meetingService.updateStatus(id, status));
    }
}

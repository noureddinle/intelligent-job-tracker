package com.jobtracker.repository;

import com.jobtracker.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByRoomId(String roomId);
}

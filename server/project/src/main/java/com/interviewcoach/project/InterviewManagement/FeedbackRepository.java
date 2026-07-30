package com.interviewcoach.project.InterviewManagement;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.models.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID>{
    
}

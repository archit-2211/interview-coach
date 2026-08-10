package com.interviewcoach.project.InterviewManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.interviewcoach.project.ProfileManagement.ProfileRepository;
import com.interviewcoach.project.auth.UserRepository;
import com.interviewcoach.project.enums.AuthenticationSource;
import com.interviewcoach.project.models.Interview;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.User;

@DataJpaTest
public class InterviewRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    InterviewRequestRepository iRequestRepository;
    @Autowired
    InterviewRepository interviewRepository;

    @Test
    void findByInterviewerEmail_success() {

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("interviewer@gmail.com");
        user.setAuthenticationSource(AuthenticationSource.LOCAL);

        userRepository.save(user);

        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID());
        profile.setUser(user);

        profileRepository.save(profile);

        InterviewRequest request = new InterviewRequest();
        request.setRequestId(UUID.randomUUID());
        request.setInterviewerProfile(profile);

        iRequestRepository.save(request);

        Interview interview = new Interview();
        interview.setInterviewId(UUID.randomUUID());
        interview.setInterviewRequest(request);

        interviewRepository.save(interview);

        List<Interview> result = interviewRepository
                .findByInterviewRequestInterviewerProfileUserEmail(
                        "interviewer@gmail.com");

        assertEquals(1, result.size());

        assertEquals(
                interview.getInterviewId(),
                result.get(0).getInterviewId());
    }

    @Test
    void findByInterviewerEmail_empty() {

        List<Interview> result = interviewRepository
                .findByInterviewRequestInterviewerProfileUserEmail(
                        "interviewer@gmail.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByCandidateEmail_success() {

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail("candidate@gmail.com");
        user.setAuthenticationSource(AuthenticationSource.LOCAL);

        userRepository.save(user);

        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID());
        profile.setUser(user);

        profileRepository.save(profile);

        InterviewRequest request = new InterviewRequest();
        request.setRequestId(UUID.randomUUID());
        request.setCandidateProfile(profile);

        iRequestRepository.save(request);

        Interview interview = new Interview();
        interview.setInterviewId(UUID.randomUUID());
        interview.setInterviewRequest(request);

        interviewRepository.save(interview);

        List<Interview> result = interviewRepository
                .findByInterviewRequestCandidateProfileUserEmail(
                        "candidate@gmail.com");

        assertEquals(1, result.size());

        assertEquals(
                interview.getInterviewId(),
                result.get(0).getInterviewId());
    }

    @Test
    void findByCandidateEmail_empty() {

        List<Interview> result = interviewRepository
                .findByInterviewRequestCandidateProfileUserEmail(
                        "candidate@gmail.com");

        assertTrue(result.isEmpty());
    }
}

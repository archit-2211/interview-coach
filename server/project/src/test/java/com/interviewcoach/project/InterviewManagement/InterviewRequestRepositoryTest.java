package com.interviewcoach.project.InterviewManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.interviewcoach.project.ProfileManagement.ProfileRepository;
import com.interviewcoach.project.SlotManagement.SlotRepository;
import com.interviewcoach.project.auth.UserRepository;
import com.interviewcoach.project.enums.AuthenticationSource;
import com.interviewcoach.project.enums.InterviewRequestStatus;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Slot;
import com.interviewcoach.project.models.User;

@DataJpaTest
public class InterviewRequestRepositoryTest {

    @Autowired
    private InterviewRequestRepository iRequestRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByCandidateProfileAndSlot_success() {
        Profile profile = new Profile();
        UUID profileId = UUID.randomUUID();
        profile.setProfileId(profileId);
        UUID slotId = UUID.randomUUID();
        Slot slot = new Slot();
        slot.setSlotId(slotId);
        slot.setProfile(profile);
        profileRepository.save(profile);
        slotRepository.save(slot);
        UUID iId = UUID.randomUUID();
        InterviewRequest iRequest = new InterviewRequest();
        iRequest.setCandidateProfile(profile);
        iRequest.setRequestId(iId);
        iRequest.setSlot(slot);
        iRequestRepository.save(iRequest);
        boolean result = iRequestRepository.existsByCandidateProfileAndSlot(profile, slot);

        assertTrue(result);

    }

    @Test
    void existsByCandidateProfileAndSlot_fail() {
        Profile profile = new Profile();
        UUID profileId = UUID.randomUUID();
        profile.setProfileId(profileId);
        UUID slotId = UUID.randomUUID();
        Slot slot = new Slot();
        slot.setSlotId(slotId);
        slot.setProfile(profile);
        profileRepository.save(profile);
        slotRepository.save(slot);
        boolean result = iRequestRepository.existsByCandidateProfileAndSlot(profile, slot);
        assertFalse(result);

    }
@Test
void findByInterviewerEmailAndStatus_success() {

    User user = new User();
    user.setUserId(UUID.randomUUID());
    user.setEmail("interviewer@gmail.com");
    user.setAuthenticationSource(AuthenticationSource.LOCAL);

    userRepository.save(user);

    Profile profile = new Profile();
    profile.setProfileId(UUID.randomUUID());
    profile.setUser(user);

    profileRepository.save(profile);

    InterviewRequest olderRequest = new InterviewRequest();
    olderRequest.setRequestId(UUID.randomUUID());
    olderRequest.setInterviewerProfile(profile);
    olderRequest.setInterviewRequestStatus(
            InterviewRequestStatus.PENDING);
    olderRequest.setCreatedAt(
            LocalDateTime.of(2026, 8, 1, 10, 0));

    InterviewRequest newerRequest = new InterviewRequest();
    newerRequest.setRequestId(UUID.randomUUID());
    newerRequest.setInterviewerProfile(profile);
    newerRequest.setInterviewRequestStatus(
            InterviewRequestStatus.PENDING);
    newerRequest.setCreatedAt(
            LocalDateTime.of(2026, 8, 2, 10, 0));

    iRequestRepository.save(olderRequest);
    iRequestRepository.save(newerRequest);

    List<InterviewRequest> result =
            iRequestRepository
                    .findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                            "interviewer@gmail.com",
                            InterviewRequestStatus.PENDING);

    assertEquals(2, result.size());

    assertEquals(
            newerRequest.getRequestId(),
            result.get(0).getRequestId());

    assertEquals(
            olderRequest.getRequestId(),
            result.get(1).getRequestId());
}

@Test
void findByInterviewerEmailAndStatus_returnsOnlyMatchingStatus() {

    User user = new User();
    user.setUserId(UUID.randomUUID());
    user.setEmail("interviewer@gmail.com");
    user.setAuthenticationSource(AuthenticationSource.LOCAL);

    userRepository.save(user);

    Profile profile = new Profile();
    profile.setProfileId(UUID.randomUUID());
    profile.setUser(user);

    profileRepository.save(profile);

    InterviewRequest pendingRequest = new InterviewRequest();
    pendingRequest.setRequestId(UUID.randomUUID());
    pendingRequest.setInterviewerProfile(profile);
    pendingRequest.setInterviewRequestStatus(
            InterviewRequestStatus.PENDING);

    InterviewRequest acceptedRequest = new InterviewRequest();
    acceptedRequest.setRequestId(UUID.randomUUID());
    acceptedRequest.setInterviewerProfile(profile);
    acceptedRequest.setInterviewRequestStatus(
            InterviewRequestStatus.ACCEPTED);

    iRequestRepository.save(pendingRequest);
    iRequestRepository.save(acceptedRequest);

    List<InterviewRequest> result =
            iRequestRepository
                    .findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                            "interviewer@gmail.com",
                            InterviewRequestStatus.PENDING);

    assertEquals(1, result.size());

    assertEquals(
            InterviewRequestStatus.PENDING,
            result.get(0).getInterviewRequestStatus());
}

}

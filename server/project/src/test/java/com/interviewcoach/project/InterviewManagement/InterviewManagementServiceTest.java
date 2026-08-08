package com.interviewcoach.project.InterviewManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interviewcoach.project.InterviewManagement.dtos.IRDetailsDTO;
import com.interviewcoach.project.InterviewManagement.dtos.InterviewDTO;
import com.interviewcoach.project.InterviewManagement.dtos.InterviewRequestDTO;
import com.interviewcoach.project.InterviewManagement.dtos.InterviewerResponseDTO;
import com.interviewcoach.project.ProfileManagement.ProfileManagementService;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileResponseDTO;
import com.interviewcoach.project.SlotManagement.SlotService;
import com.interviewcoach.project.enums.InterviewRequestStatus;
import com.interviewcoach.project.enums.InterviewStatus;
import com.interviewcoach.project.enums.SlotStatus;
import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.enums.UserStatus;
import com.interviewcoach.project.models.Interview;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Skill;
import com.interviewcoach.project.models.Slot;
import com.interviewcoach.project.models.User;
import com.interviewcoach.project.security.JwtService;

/*
Not all methods are covered in the testcases, every concepot of mockito has been covered 
effectively

*/

@ExtendWith(MockitoExtension.class)
public class InterviewManagementServiceTest {
    @Mock
    private InterviewRepository interviewRepository;
    @Mock
    private ProfileManagementService pmService;
    @Mock
    private SlotService slotService;
    @Mock
    private InterviewRequestRepository interviewRequestRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    InterviewManagementService imService;

    @Test
    void shouldReturnInterviewersSuccessfully() {

        // Arrange
        List<String> skills = List.of("Java", "Spring");

        int pageNumber = 0;
        int pageSize = 10;

        ProfileResponseDTO profile = new ProfileResponseDTO(
                "interviewer@test.com",
                "John",
                "9432345693", UserRole.CANDIDATE, UserStatus.VERIFIED, 4.5,
                List.of("Java", "Spring"));

        when(pmService.getInterviewers(skills, pageNumber, pageSize))
                .thenReturn(List.of(profile));

        // Act
        List<InterviewerResponseDTO> result = imService.getInterviewers(skills, pageNumber, pageSize);

        // Assert
        assertEquals(1, result.size());

        InterviewerResponseDTO interviewer = result.get(0);

        assertEquals("interviewer@test.com", interviewer.email());
        assertEquals("John", interviewer.fullName());
        assertEquals(4.5, interviewer.rating());
        assertEquals(List.of("Java", "Spring"), interviewer.skills());

        // Verify
        verify(pmService)
                .getInterviewers(skills, pageNumber, pageSize);
    }

    @Test
    void checkEmptyInterviewers() {
        List<String> skills = List.of("Java", "Spring");

        int pageNumber = 0;
        int pageSize = 10;

        when(pmService.getInterviewers(skills, pageNumber, pageSize)).thenReturn(List.of());
        List<InterviewerResponseDTO> result = imService.getInterviewers(skills, pageNumber, pageSize);
        assertTrue(result.isEmpty());
        verify(pmService).getInterviewers(skills, pageNumber, pageSize);

    }

    @Test
    void createInterviewRequest_successfulCreation() {

        // Arrange
        IRDetailsDTO dto = new IRDetailsDTO(
                UUID.randomUUID(),
                "interviewer@gmail.com",
                List.of("Java", "DSA"),
                "Please be prepared");

        String userEmail = "user@email.com";

        User candidateUser = new User();
        candidateUser.setEmail(userEmail);

        Profile candidateProfile = new Profile();
        candidateProfile.setUser(candidateUser);

        User interviewerUser = new User();
        interviewerUser.setEmail(dto.interviewerEmail());

        Profile interviewerProfile = new Profile();
        interviewerProfile.setUser(interviewerUser);

        Slot slot = new Slot();
        slot.setSlotId(dto.slotId());
        slot.setProfile(interviewerProfile);
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        List<Skill> skills = dto.topics()
                .stream()
                .map(ele -> {
                    Skill skill = new Skill();
                    skill.setSkillName(ele);
                    return skill;
                })
                .toList();

        when(pmService.getProfileUsingEmail(userEmail))
                .thenReturn(candidateProfile);

        when(slotService.getSlotById(dto.slotId()))
                .thenReturn(slot);

        when(interviewRequestRepository
                .existsByCandidateProfileAndSlot(candidateProfile, slot))
                .thenReturn(false);

        when(pmService.getAllSkills(dto.topics()))
                .thenReturn(skills);

        // Act
        imService.createInterviewRequest(dto, userEmail);

        // Capture
        ArgumentCaptor<InterviewRequest> captor = ArgumentCaptor.forClass(InterviewRequest.class);

        verify(interviewRequestRepository)
                .save(captor.capture());

        InterviewRequest request = captor.getValue();

        assertEquals(
                userEmail,
                request.getCandidateProfile().getUser().getEmail());

        assertEquals(
                dto.interviewerEmail(),
                request.getInterviewerProfile().getUser().getEmail());

        assertEquals(
                slot.getSlotId(),
                request.getSlot().getSlotId());

        assertEquals(
                SlotStatus.AVAILABLE,
                request.getSlot().getSlotStatus());

        assertEquals(
                InterviewRequestStatus.PENDING,
                request.getInterviewRequestStatus());

        assertEquals(
                dto.description(),
                request.getCandidateNotes());

        assertEquals(
                skills,
                request.getTopics());

        // Verify dependencies
        verify(pmService)
                .getProfileUsingEmail(userEmail);

        verify(slotService)
                .getSlotById(dto.slotId());

        verify(interviewRequestRepository)
                .existsByCandidateProfileAndSlot(candidateProfile, slot);

        verify(pmService)
                .getAllSkills(dto.topics());
    }

    @Test
    void createInterviewRequest_invalidInterviewerEmail() {

        // Arrange
        IRDetailsDTO dto = new IRDetailsDTO(
                UUID.randomUUID(),
                "interviewer@gmail.com",
                List.of("Java", "DSA"),
                "Please be prepared");

        String userEmail = "user@email.com";

        User candidateUser = new User();
        candidateUser.setEmail(userEmail);

        Profile candidateProfile = new Profile();
        candidateProfile.setUser(candidateUser);

        User differentInterviewerUser = new User();
        differentInterviewerUser.setEmail("wrong@gmail.com");

        Profile interviewerProfile = new Profile();
        interviewerProfile.setUser(differentInterviewerUser);

        Slot slot = new Slot();
        slot.setSlotId(dto.slotId());
        slot.setProfile(interviewerProfile);
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        when(pmService.getProfileUsingEmail(userEmail))
                .thenReturn(candidateProfile);

        when(slotService.getSlotById(dto.slotId()))
                .thenReturn(slot);

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.createInterviewRequest(dto, userEmail));

        assertEquals("INVALID DETAILS PROVIDED.", exception.getMessage());

        // Verify
        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));
    }

    @Test
    void createInterviewRequest_candidateRequestsOwnSlot() {

        // Arrange
        IRDetailsDTO dto = new IRDetailsDTO(
                UUID.randomUUID(),
                "interviewer@gmail.com",
                List.of("Java", "DSA"),
                "Please be prepared");

        String userEmail = "user@email.com";

        User user = new User();
        user.setEmail(userEmail);

        Profile candidateProfile = new Profile();
        candidateProfile.setProfileId(UUID.randomUUID());
        candidateProfile.setUser(user);

        Profile interviewerProfile = new Profile();
        interviewerProfile.setProfileId(candidateProfile.getProfileId());

        User interviewerUser = new User();
        interviewerUser.setEmail(dto.interviewerEmail());
        interviewerProfile.setUser(interviewerUser);

        Slot slot = new Slot();
        slot.setSlotId(dto.slotId());
        slot.setProfile(interviewerProfile);
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        when(pmService.getProfileUsingEmail(userEmail))
                .thenReturn(candidateProfile);

        when(slotService.getSlotById(dto.slotId()))
                .thenReturn(slot);

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.createInterviewRequest(dto, userEmail));

        assertEquals(
                "You cannot request your own slot",
                exception.getMessage());

        // Verify
        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));
    }

    @Test
    void createInterviewRequest_slotNotAvailable() {

        // Arrange
        IRDetailsDTO dto = new IRDetailsDTO(
                UUID.randomUUID(),
                "interviewer@gmail.com",
                List.of("Java", "DSA"),
                "Please be prepared");

        String userEmail = "user@email.com";

        User candidateUser = new User();
        candidateUser.setEmail(userEmail);

        Profile candidateProfile = new Profile();
        candidateProfile.setProfileId(UUID.randomUUID());
        candidateProfile.setUser(candidateUser);

        User interviewerUser = new User();
        interviewerUser.setEmail(dto.interviewerEmail());

        Profile interviewerProfile = new Profile();
        interviewerProfile.setProfileId(UUID.randomUUID());
        interviewerProfile.setUser(interviewerUser);

        Slot slot = new Slot();
        slot.setSlotId(dto.slotId());
        slot.setProfile(interviewerProfile);
        slot.setSlotStatus(SlotStatus.BOOKED);

        when(pmService.getProfileUsingEmail(userEmail))
                .thenReturn(candidateProfile);

        when(slotService.getSlotById(dto.slotId()))
                .thenReturn(slot);

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.createInterviewRequest(dto, userEmail));

        assertEquals(
                "Slot is not available",
                exception.getMessage());

        // Verify
        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));
    }

    @Test
    void createInterviewRequest_requestAlreadyExists() {

        // Arrange
        IRDetailsDTO dto = new IRDetailsDTO(
                UUID.randomUUID(),
                "interviewer@gmail.com",
                List.of("Java", "DSA"),
                "Please be prepared");

        String userEmail = "user@email.com";

        User candidateUser = new User();
        candidateUser.setEmail(userEmail);

        Profile candidateProfile = new Profile();
        candidateProfile.setProfileId(UUID.randomUUID());
        candidateProfile.setUser(candidateUser);

        User interviewerUser = new User();
        interviewerUser.setEmail(dto.interviewerEmail());

        Profile interviewerProfile = new Profile();
        interviewerProfile.setProfileId(UUID.randomUUID());
        interviewerProfile.setUser(interviewerUser);

        Slot slot = new Slot();
        slot.setSlotId(dto.slotId());
        slot.setProfile(interviewerProfile);
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        when(pmService.getProfileUsingEmail(userEmail))
                .thenReturn(candidateProfile);

        when(slotService.getSlotById(dto.slotId()))
                .thenReturn(slot);

        when(interviewRequestRepository
                .existsByCandidateProfileAndSlot(candidateProfile, slot))
                .thenReturn(true);

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.createInterviewRequest(dto, userEmail));

        assertEquals(
                "Interview request already exists",
                exception.getMessage());

        // Verify
        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));
    }

    @Test
    void getAllInterviewRequests_asInterviewer() {

        String email = "interviewer@gmail.com";

        InterviewRequest request = new InterviewRequest();
        List<InterviewRequest> requests = List.of(request);

        when(jwtService.isInterviewer()).thenReturn(true);

        when(interviewRequestRepository
                .findByInterviewerProfileUserEmailOrderByCreatedAtDesc(email))
                .thenReturn(requests);

        List result = imService.getAllInterviewRequests(email);

        assertEquals(1, result.size());

        verify(jwtService).isInterviewer();

        verify(interviewRequestRepository)
                .findByInterviewerProfileUserEmailOrderByCreatedAtDesc(email);

        verify(interviewRequestRepository, never())
                .findByCandidateProfileUserEmailOrderByCreatedAtDesc(anyString());
    }

    @Test
    void getAllInterviewRequests_asCandidate() {

        String email = "candidate@gmail.com";

        InterviewRequest request = new InterviewRequest();
        List<InterviewRequest> requests = List.of(request);

        when(jwtService.isInterviewer()).thenReturn(false);

        when(interviewRequestRepository
                .findByCandidateProfileUserEmailOrderByCreatedAtDesc(email))
                .thenReturn(requests);

        List result = imService.getAllInterviewRequests(email);

        assertEquals(1, result.size());

        verify(jwtService).isInterviewer();

        verify(interviewRequestRepository)
                .findByCandidateProfileUserEmailOrderByCreatedAtDesc(email);

        verify(interviewRequestRepository, never())
                .findByInterviewerProfileUserEmailOrderByCreatedAtDesc(anyString());
    }

    @Test
    void getPendingInterviewRequests_asInterviewer() {

        String email = "interviewer@gmail.com";

        InterviewRequest request = new InterviewRequest();

        when(jwtService.isInterviewer()).thenReturn(true);

        when(interviewRequestRepository
                .findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                        email,
                        InterviewRequestStatus.PENDING))
                .thenReturn(List.of(request));

        List<InterviewRequestDTO> result = imService.getPendingInterviewRequests(email);

        assertEquals(1, result.size());

        verify(interviewRequestRepository)
                .findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                        email,
                        InterviewRequestStatus.PENDING);

        verify(interviewRequestRepository, never())
                .findByCandidateProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                        anyString(),
                        any(InterviewRequestStatus.class));
    }

    @Test
    void getPendingInterviewRequests_asCandidate() {

        String email = "candidate@gmail.com";

        InterviewRequest request = new InterviewRequest();

        when(jwtService.isInterviewer()).thenReturn(false);

        when(interviewRequestRepository
                .findByCandidateProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                        email,
                        InterviewRequestStatus.PENDING))
                .thenReturn(List.of(request));

        List<InterviewRequestDTO> result = imService.getPendingInterviewRequests(email);

        assertEquals(1, result.size());

        verify(interviewRequestRepository)
                .findByCandidateProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                        email,
                        InterviewRequestStatus.PENDING);

        verify(interviewRequestRepository, never())
                .findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                        anyString(),
                        any(InterviewRequestStatus.class));
    }

    @Test
    void acceptInterviewRequest_success() {

        String email = "interviewer@gmail.com";
        UUID requestId = UUID.randomUUID();
        String meetingLink = "https://meet.google.com/test";

        Profile interviewerProfile = new Profile();

        User interviewerUser = new User();
        interviewerUser.setEmail(email);
        interviewerProfile.setUser(interviewerUser);

        Slot slot = new Slot();
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        InterviewRequest request = new InterviewRequest();
        request.setInterviewerProfile(interviewerProfile);
        request.setSlot(slot);
        request.setInterviewRequestStatus(InterviewRequestStatus.PENDING);

        when(interviewRequestRepository.findById(requestId))
                .thenReturn(Optional.of(request));

        when(slotService.setSlotToBooked(slot))
                .thenReturn(slot);

        when(interviewRequestRepository.save(request))
                .thenReturn(request);

        Interview interview = new Interview();

        // If getInterview() is private, let the real method execute.
        // Make sure request/interview fields required by your mapper are initialized.

        when(interviewRepository.save(any(Interview.class)))
                .thenReturn(interview);

        InterviewDTO result = imService.acceptInterviewRequest(
                email,
                requestId,
                meetingLink);

        assertEquals(
                InterviewRequestStatus.ACCEPTED,
                request.getInterviewRequestStatus());

        assertSame(slot, request.getSlot());

        verify(interviewRequestRepository)
                .findById(requestId);

        verify(slotService)
                .setSlotToBooked(slot);

        verify(interviewRequestRepository)
                .save(request);

        verify(interviewRepository)
                .save(any(Interview.class));
    }

    @Test
    void acceptInterviewRequest_requestNotFound() {

        UUID requestId = UUID.randomUUID();

        when(interviewRequestRepository.findById(requestId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.acceptInterviewRequest(
                        "interviewer@gmail.com",
                        requestId,
                        "meeting-link"));

        assertEquals(
                "InterviewRequest not found",
                exception.getMessage());

        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));

        verify(interviewRepository, never())
                .save(any(Interview.class));
    }

    @Test
    void acceptInterviewRequest_requestNotPending() {

        String email = "interviewer@gmail.com";
        UUID requestId = UUID.randomUUID();

        Profile interviewerProfile = new Profile();

        User user = new User();
        user.setEmail(email);
        interviewerProfile.setUser(user);

        Slot slot = new Slot();
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        InterviewRequest request = new InterviewRequest();

        request.setInterviewerProfile(interviewerProfile);
        request.setSlot(slot);
        request.setInterviewRequestStatus(
                InterviewRequestStatus.ACCEPTED);

        when(interviewRequestRepository.findById(requestId))
                .thenReturn(Optional.of(request));

        when(slotService.setSlotToBooked(slot))
                .thenReturn(slot);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.acceptInterviewRequest(
                        email,
                        requestId,
                        "meeting-link"));

        assertEquals(
                "Interview request is not pending",
                exception.getMessage());

        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));

        verify(interviewRepository, never())
                .save(any(Interview.class));
    }

    @Test
    void acceptInterviewRequest_unauthorizedInterviewer() {

        String email = "wrong@gmail.com";
        UUID requestId = UUID.randomUUID();

        Profile interviewerProfile = new Profile();

        User user = new User();
        user.setEmail("realinterviewer@gmail.com");
        interviewerProfile.setUser(user);

        InterviewRequest request = new InterviewRequest();
        request.setInterviewerProfile(interviewerProfile);

        when(interviewRequestRepository.findById(requestId))
                .thenReturn(Optional.of(request));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.acceptInterviewRequest(
                        email,
                        requestId,
                        "meeting-link"));

        verify(interviewRequestRepository, never())
                .save(any(InterviewRequest.class));

        verify(interviewRepository, never())
                .save(any(Interview.class));
    }

    @Test
    void getMyInterviews_asInterviewer() {

        String email = "interviewer@gmail.com";

        Interview interview = new Interview();

        when(jwtService.isInterviewer())
                .thenReturn(true);

        when(interviewRepository
                .findByInterviewRequestInterviewerProfileUserEmail(email))
                .thenReturn(List.of(interview));

        List<InterviewDTO> result = imService.getMyInterviews(email);

        assertEquals(1, result.size());

        verify(interviewRepository)
                .findByInterviewRequestInterviewerProfileUserEmail(email);

        verify(interviewRepository, never())
                .findByInterviewRequestCandidateProfileUserEmail(anyString());
    }

    @Test
    void getMyInterviews_asCandidate() {

        String email = "candidate@gmail.com";

        Interview interview = new Interview();

        when(jwtService.isInterviewer())
                .thenReturn(false);

        when(interviewRepository
                .findByInterviewRequestCandidateProfileUserEmail(email))
                .thenReturn(List.of(interview));

        List<InterviewDTO> result = imService.getMyInterviews(email);

        assertEquals(1, result.size());

        verify(interviewRepository)
                .findByInterviewRequestCandidateProfileUserEmail(email);

        verify(interviewRepository, never())
                .findByInterviewRequestInterviewerProfileUserEmail(anyString());
    }

    @Test
    void completeInterview_success() {

        UUID interviewId = UUID.randomUUID();
        String email = "interviewer@gmail.com";

        Profile interviewerProfile = new Profile();

        User user = new User();
        user.setEmail(email);
        interviewerProfile.setUser(user);

        InterviewRequest request = new InterviewRequest();
        request.setInterviewerProfile(interviewerProfile);

        Interview interview = new Interview();
        interview.setInterviewRequest(request);

        when(interviewRepository.findById(interviewId))
                .thenReturn(Optional.of(interview));

        when(interviewRepository.save(interview))
                .thenReturn(interview);

        InterviewDTO result = imService.completeInterview(
                interviewId,
                email);

        assertEquals(
                InterviewStatus.COMPLETED,
                interview.getInterviewStatus());

        verify(pmService)
                .incrementCount(interviewerProfile);

        verify(interviewRepository)
                .save(interview);
    }

    @Test
    void completeInterview_notFound() {

        UUID interviewId = UUID.randomUUID();

        when(interviewRepository.findById(interviewId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.completeInterview(
                        interviewId,
                        "interviewer@gmail.com"));

        assertEquals(
                "Invalid Interview Id",
                exception.getMessage());

        verify(interviewRepository, never())
                .save(any());
    }

    @Test
    void completeInterview_unauthorizedInterviewer() {

        UUID interviewId = UUID.randomUUID();

        Profile profile = new Profile();

        User user = new User();
        user.setEmail("real@gmail.com");
        profile.setUser(user);

        InterviewRequest request = new InterviewRequest();
        request.setInterviewerProfile(profile);

        Interview interview = new Interview();
        interview.setInterviewRequest(request);

        when(interviewRepository.findById(interviewId))
                .thenReturn(Optional.of(interview));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> imService.completeInterview(
                        interviewId,
                        "wrong@gmail.com"));

        verify(interviewRepository, never())
                .save(any());
    }

}

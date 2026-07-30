package com.interviewcoach.project.InterviewManagement;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.interviewcoach.project.InterviewManagement.dtos.FeedbackDTO;
import com.interviewcoach.project.InterviewManagement.dtos.FeedbackResponseDTO;
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
import com.interviewcoach.project.models.CandidatesAssessment;
import com.interviewcoach.project.models.Feedback;
import com.interviewcoach.project.models.Interview;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.InterviewersFeedback;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Skill;
import com.interviewcoach.project.models.Slot;

import jakarta.transaction.Transactional;

@Service
public class InterviewManagementService {

        private InterviewRepository interviewRepository;
        private ProfileManagementService pmService;
        private SlotService slotService;
        private InterviewRequestRepository interviewRequestRepository;

        public InterviewManagementService(ProfileManagementService pmService, SlotService slotService,
                        InterviewRequestRepository interviewRequestRepository,
                        InterviewRepository interviewRepository) {
                this.pmService = pmService;
                this.slotService = slotService;
                this.interviewRequestRepository = interviewRequestRepository;
                this.interviewRepository = interviewRepository;

        }

        @PreAuthorize("hasRole('CANDIDATE')")
        public List<InterviewerResponseDTO> getInterviewers(List<String> skills, int pageNumber, int pageSize) {

                List<ProfileResponseDTO> interviewers = pmService.getInterviewers(skills, pageNumber, pageSize);

                return interviewers.stream().map(ele -> mapToDto(ele)).toList();
        }

        @PreAuthorize("hasRole('CANDIDATE')")
        @Transactional
        public void createInterviewRequest(
                        IRDetailsDTO dto,
                        String userEmail) {

                Profile candidateProfile = pmService.getProfileUsingEmail(userEmail);

                Slot slot = slotService.getSlotById(dto.slotId());

                Profile interviewerProfile = slot.getProfile();
                System.out.println(interviewerProfile.getUser().getEmail() + dto.interviewerEmail());
                if (!interviewerProfile.getUser().getEmail().equals(dto.interviewerEmail())) {
                        throw new RuntimeException("INVALID DETAILS PROVIDED.");
                }

                if (candidateProfile.getProfileId()
                                .equals(interviewerProfile.getProfileId())) {

                        throw new RuntimeException(
                                        "You cannot request your own slot");
                }

                if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {

                        throw new RuntimeException(
                                        "Slot is not available");
                }

                boolean alreadyRequested = interviewRequestRepository
                                .existsByCandidateProfileAndSlot(
                                                candidateProfile,
                                                slot);

                if (alreadyRequested) {

                        throw new RuntimeException(
                                        "Interview request already exists");
                }

                List<Skill> topics = pmService.getAllSkills(
                                dto.topics());

                InterviewRequest request = new InterviewRequest();

                request.setRequestId(
                                UUID.randomUUID());

                request.setCandidateProfile(
                                candidateProfile);

                request.setInterviewerProfile(
                                interviewerProfile);

                request.setSlot(
                                slot);

                request.setTopics(
                                topics);

                request.setCandidateNotes(
                                dto.description());

                request.setInterviewRequestStatus(
                                InterviewRequestStatus.PENDING);

                interviewRequestRepository
                                .save(request);

        }

        public List<InterviewRequestDTO> getAllInterviewRequests(String email) {
                List<InterviewRequest> allRequests;

                if (isInterviewer()) {
                        allRequests = interviewRequestRepository
                                        .findByInterviewerProfileUserEmailOrderByCreatedAtDesc(email);

                } else {
                        allRequests = interviewRequestRepository
                                        .findByCandidateProfileUserEmailOrderByCreatedAtDesc(email);
                }

                return allRequests.stream().map(ele -> mapToDto(ele)).toList();

        }

        public List<InterviewRequestDTO> getPendingInterviewRequests(String email) {

                List<InterviewRequest> allRequests;

                if (isInterviewer()) {
                        allRequests = interviewRequestRepository
                                        .findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                                                        email, InterviewRequestStatus.PENDING);

                } else {
                        allRequests = interviewRequestRepository
                                        .findByCandidateProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(
                                                        email, InterviewRequestStatus.PENDING);
                }

                return allRequests.stream().map(ele -> mapToDto(ele)).toList();

        }

        @PreAuthorize("hasRole('INTERVIEWER')")
        @Transactional
        public InterviewDTO acceptInterviewRequest(String email, UUID requestId, String meetingLink) {
                InterviewRequest request = interviewRequestRepository.findById(requestId)
                                .orElseThrow(() -> new RuntimeException("InterviewRequest not found"));
                Profile interviewProfile = request.getInterviewerProfile();
                validateProfile(interviewProfile, email);
                request.setSlot(slotService.setSlotToBooked(request.getSlot()));
                if (!request.getInterviewRequestStatus()
                                .equals(InterviewRequestStatus.PENDING)) {

                        throw new RuntimeException(
                                        "Interview request is not pending");
                }
                request.setInterviewRequestStatus(InterviewRequestStatus.ACCEPTED);
                request = interviewRequestRepository.save(request);
                Interview interview = getInterview(request, meetingLink);

                interview = interviewRepository.save(interview);

                return mapToDto(interview);

        }

        @PreAuthorize("hasRole('INTERVIEWER')")
        public String rejectInterviewRequest(
                        String email,
                        UUID requestId) {

                InterviewRequest request = interviewRequestRepository.findById(requestId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Interview request not found"));

                Profile interviewerProfile = request.getInterviewerProfile();

                validateProfile(
                                interviewerProfile,
                                email);

                if (request.getInterviewRequestStatus() != InterviewRequestStatus.PENDING) {

                        throw new RuntimeException(
                                        "Only pending requests can be rejected");
                }

                request.setInterviewRequestStatus(
                                InterviewRequestStatus.REJECTED);

                interviewRequestRepository.save(
                                request);

                return "Interview request rejected successfully";
        }

        public String cancelInterviewRequest(
                        String email,
                        UUID requestId) {
           
                InterviewRequest request = interviewRequestRepository.findById(requestId)
                                .orElseThrow(() -> new RuntimeException(
                                                "InterviewRequest not found"));

                Profile candidateProfile = request.getCandidateProfile();

                validateProfile(
                                candidateProfile,
                                email);

                if (request.getInterviewRequestStatus() != InterviewRequestStatus.PENDING) {

                        throw new RuntimeException(
                                        "Operation of cancelling interview request is not possible");
                }

                request.setInterviewRequestStatus(
                                InterviewRequestStatus.CANCELLED);

                interviewRequestRepository.save(
                                request);

                return "SUCCESS";
        }

        public List<InterviewDTO> getMyInterviews(String email) {

                List<Interview> interviews;

                if (isInterviewer()) {

                        interviews = interviewRepository
                                        .findByInterviewRequestInterviewerProfileUserEmail(
                                                        email);

                } else {

                        interviews = interviewRepository
                                        .findByInterviewRequestCandidateProfileUserEmail(
                                                        email);
                }

                return interviews.stream()
                                .map(this::mapToDto)
                                .toList();
        }

        @Transactional
        @PreAuthorize("hasRole('INTERVIEWER')")
        public InterviewDTO completeInterview(UUID interviewId, String email) {
                Interview interview = interviewRepository.findById(interviewId)
                                .orElseThrow(() -> new RuntimeException("Invalid Interview Id"));
                Profile interviewerProfile = interview.getInterviewRequest().getInterviewerProfile();
                pmService.incrementCount(interviewerProfile);
                validateProfile(interviewerProfile, email);
                interview.setInterviewStatus(InterviewStatus.COMPLETED);
                interview = interviewRepository.save(interview);

                return mapToDto(interview);

        }

        @Transactional
        @PreAuthorize("hasAnyRole('CANDIDATE','INTERVIEWER')")
        public String cancelInterview(
                        UUID interviewId,
                        String email) {

                Interview interview = interviewRepository.findById(interviewId)
                                .orElseThrow(() -> new RuntimeException("Interview not found"));

                InterviewRequest request = interview.getInterviewRequest();

                boolean isCandidate = request.getCandidateProfile()
                                .getUser()
                                .getEmail()
                                .equals(email);

                boolean isInterviewer = request.getInterviewerProfile()
                                .getUser()
                                .getEmail()
                                .equals(email);

                if (!isCandidate && !isInterviewer) {
                        throw new RuntimeException(
                                        "You are not authorized to cancel this interview");
                }

                if (interview.getInterviewStatus() != InterviewStatus.SCHEDULED) {

                        throw new RuntimeException(
                                        "Interview cannot be cancelled");
                }

                interview.setInterviewStatus(
                                InterviewStatus.CANCELLED);

                Slot slot = request.getSlot();
                slot.setSlotStatus(
                                SlotStatus.AVAILABLE);

                return "SUCCESS";
        }

        protected Interview getInterviewById(UUID interviewId) {
                return interviewRepository.findById(interviewId).orElseThrow(
                                () -> new RuntimeException("Interview not found. Not a valid InterviewId"));
        }

        private Interview getInterview(InterviewRequest request, String meetingLink) {
                Interview interview = new Interview();
                interview.setInterviewId(UUID.randomUUID());
                interview.setInterviewStatus(InterviewStatus.SCHEDULED);
                interview.setInterviewRequest(request);
                interview.setMeetingLink(meetingLink);

                Feedback myFeedback = new Feedback();
                myFeedback.setCandidatesAssessment(new CandidatesAssessment());
                myFeedback.setInterviewersFeedback(new InterviewersFeedback());
                myFeedback.setFeedbackId(UUID.randomUUID());

                interview.setFeedback(myFeedback);

                return interview;

        }
        protected void markFeedbackSubmitted(Interview interview) {
                interviewRepository.save(interview) ; 
        }
        protected void markAssessmentSubmitted(Interview interview) {
                interviewRepository.save(interview) ; 
        }

        protected void validateProfile(Profile profile, String email) {
                if (!profile.getUser().getEmail().equals(email)) {
                        throw new RuntimeException("You don't have access to it");
                }
        }

        private InterviewerResponseDTO mapToDto(ProfileResponseDTO response) {
                InterviewerResponseDTO myresponse = new InterviewerResponseDTO(response.email(), response.name(),
                                response.rating(), response.skills());
                return myresponse;
        }

        private InterviewRequestDTO mapToDto(InterviewRequest request) {
                List<String> allTopics = request.getTopics().stream().map((ele) -> ele.getSkillName()).toList();
                String candidateEmail = request.getCandidateProfile().getUser().getEmail();
                String interviewerEmail = request.getInterviewerProfile().getUser().getEmail();
                Slot slot = request.getSlot();
                return new InterviewRequestDTO(request.getRequestId(), slot.getSlotId(), request.getCandidateNotes(),
                                allTopics, request.getInterviewRequestStatus(), candidateEmail, interviewerEmail,
                                slot.getSlotTiming().getDate(), slot.getSlotTiming().getStartTime(),
                                slot.getSlotTiming().getEndTime());

        }

        private InterviewDTO mapToDto(Interview interview) {
                InterviewRequest request = interview.getInterviewRequest();
                Feedback feedback = interview.getFeedback() ; 
                FeedbackResponseDTO fbDTO = new FeedbackResponseDTO(feedback.getCandidatesAssessment().getAssessment(),
                 feedback.getInterviewersFeedback().getComments(), feedback.getCandidatesAssessment().getCandidateRating(), feedback.getInterviewersFeedback().getInterviewerRating()) ; 

                InterviewDTO dto = new InterviewDTO(interview.getInterviewId(),
                                request.getCandidateProfile().getUser().getEmail(),
                                request.getInterviewerProfile().getUser().getEmail(), interview.getInterviewStatus(),
                                interview.getMeetingLink(), request.getSlot().getSlotTiming().getDate(),
                                request.getSlot().getSlotTiming().getStartTime(),
                                request.getSlot().getSlotTiming().getEndTime(), fbDTO, interview.isFeedbackSubmitted(), interview.isAssessmentSubmitted());
                return dto;
        }

      

        protected boolean isInterviewer() {
                Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                boolean isInterviewer = auth.getAuthorities()
                                .stream()
                                .anyMatch(
                                                authority -> authority.getAuthority()
                                                                .equals("ROLE_INTERVIEWER"));
                return isInterviewer;

        }
}

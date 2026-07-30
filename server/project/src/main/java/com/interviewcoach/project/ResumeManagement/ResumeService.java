package com.interviewcoach.project.ResumeManagement;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interviewcoach.project.GlobalExceptions.UnauthorisedException;
import com.interviewcoach.project.ProfileManagement.ProfileRepository;
import com.interviewcoach.project.ProfileManagement.exceptions.EmptyFileException;
import com.interviewcoach.project.ProfileManagement.exceptions.InvalidFileTypeException;
import com.interviewcoach.project.ResumeManagement.dto.ResumeResponseDTO;
import com.interviewcoach.project.ResumeManagement.exceptions.ProfileNotFoundException;
import com.interviewcoach.project.ResumeManagement.exceptions.ResumeNotFoundException;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Resume;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ProfileRepository profileRepository;

    public ResumeService(
            ResumeRepository resumeRepository,
            ProfileRepository profileRepository) {
        this.resumeRepository = resumeRepository;
        this.profileRepository = profileRepository;
    }


    public ResumeResponseDTO uploadResume(
            MultipartFile file,
            String email) {

        fileValidation(file);

        Profile profile = getProfileByUserEmail(email);

        // Handle upload to s3 here
        // Temporary fake URL
        String fileUrl = "/uploads/" +
                file.getOriginalFilename();

        ////////////////////////
        ///

        Resume resume = new Resume();

        resume.setResumeId(
                UUID.randomUUID());

        resume.setFileName(
                file.getOriginalFilename());

        resume.setFileUrl(
                fileUrl);

        resume.setProfile(
                profile);

        Resume savedResume = resumeRepository.save(
                resume);

        return mapToDto(savedResume); 
    }

    public List<ResumeResponseDTO> getMyResumes(
            String email) {

        Profile profile = getProfileByUserEmail(email);

        return resumeRepository
                .findByProfile(profile)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteResume(
            UUID resumeId,
            String email) {

        Profile profile = getProfileByUserEmail(email);

        Resume resume = resumeRepository
                .findById(resumeId)
                .orElseThrow(
                        () -> new ResumeNotFoundException(
                                "No Resume found"));

        if (!resume.getProfile()
                .getProfileId()
                .equals(profile.getProfileId())) {

            throw new UnauthorisedException(
                    "Unauthorized");
        }

        resumeRepository.delete(resume);
    }

    private ResumeResponseDTO mapToDto(
            Resume resume) {

        return new ResumeResponseDTO(
                resume.getResumeId(),
                resume.getFileName(),
                resume.getFileUrl(),
                resume.getUploadedAt());
    }



    private void fileValidation(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException(
                    "File cannot be empty");
        }

        if (!"application/pdf".equals(
                file.getContentType())) {
            throw new InvalidFileTypeException(
                    "Only PDF files are allowed");
        }

    }

    private Profile getProfileByUserEmail(String email) {
        Profile profile = profileRepository
                .findByUserEmail(email)
                .orElseThrow(
                        () -> new ProfileNotFoundException(
                                "Profile not found"));
        return profile;

    }
  
}

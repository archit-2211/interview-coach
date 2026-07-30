package com.interviewcoach.project.ProfileManagement;

import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Skill;
import com.interviewcoach.project.models.User;
import com.interviewcoach.project.models.WorkExperience;


import jakarta.transaction.Transactional;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.interviewcoach.project.GlobalExceptions.UnauthorisedException;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileResponseDTO;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileSkillsDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexCreationDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexResponseDTO;
import com.interviewcoach.project.ProfileManagement.exceptions.InvalidExperienceException;
import com.interviewcoach.project.ProfileManagement.exceptions.UserNotFoundException;

@Service
public class ProfileManagementService {
    private ProfileRepository profileRepository;
    private SkillsRepository skillsRepository;
    private WorkExperienceRepository workExperienceRepository;

    public ProfileManagementService(ProfileRepository profileRepository, SkillsRepository skillsRepository,
            WorkExperienceRepository workExperienceRepository) {
        this.profileRepository = profileRepository;
        this.skillsRepository = skillsRepository;
        this.workExperienceRepository = workExperienceRepository;
    }

    public Profile createProfile(User user) {
        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID());
        profile.setUser(user);
        profile = profileRepository.save(profile);

        return profile;

    }

    public ProfileResponseDTO getProfile(String userEmail) {

        Profile profile = getProfileUsingEmail(userEmail);

        return getResponseDto(profile);

    }

    @Transactional
    public ProfileSkillsDTO updateSkills(
            List<String> skillNames,
            String userEmail) {

        Profile profile = getProfileUsingEmail(userEmail);

        List<String> normalizedSkills = normaliseSkills(skillNames);

        List<Skill> existingSkills = skillsRepository.findBySkillNameIn(
                normalizedSkills);

        Set<String> existingSkillNames = existingSkills.stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toSet());

        List<Skill> newSkills = normalizedSkills.stream()
                .filter(skill -> !existingSkillNames.contains(skill))
                .map(skill -> {

                    Skill s = new Skill();

                    s.setSkillId(
                            UUID.randomUUID());

                    s.setSkillName(skill);

                    return s;
                })
                .toList();

        List<Skill> savedSkills = skillsRepository.saveAll(
                newSkills);

        List<Skill> finalSkills = new ArrayList<>();

        finalSkills.addAll(existingSkills);
        finalSkills.addAll(savedSkills);

        

        profile.getSkills().clear();

        profile.getSkills().addAll(
                finalSkills);

        Profile savedProfile = profileRepository.save(
                profile);

        return getDto(savedProfile.getSkills()) ; 
    }

    @Transactional
    public ProfileResponseDTO removeSkillFromProfile(List<String> skills, String email) {
        Profile profile = getProfileUsingEmail(email);
        List<String> normalisedSkills = normaliseSkills(skills);
        Set<String> skillSet = new HashSet<>(normalisedSkills);
        profile.getSkills().removeIf(
                skill -> skillSet.contains(skill.getSkillName()));

        profile = profileRepository.save(profile);

        return getResponseDto(profile);

    }

    @Transactional
    public WorkexResponseDTO addWorkex(String email, WorkexCreationDTO dto) {
    
        workexValidation(dto);
        Profile profile = getProfileUsingEmail(email);
    

        WorkExperience workExperience = WorkExperience.builder().workExperienceId(UUID.randomUUID()).companyName(dto.companyName()).startDate(dto.startDate()).endDate(dto.endDate()).profile(profile).build(); 

        workExperience = workExperienceRepository.save(workExperience) ; 
        
        return new WorkexResponseDTO(getWorkExperiences(email));
    }
    @Transactional
    public void deleteWorkex(UUID id, String email) {
        System.out.println("Request reached servicec\n \n \n ");
        System.out.println(id) ; 
        boolean check = validteOwnership(id, email);
        if(!check) {
            throw new UnauthorisedException("Invalid Request") ; 
        }
        System.out.println("Reaching here");
        workExperienceRepository.deleteWorkExperienceById(id);
      
        

    }
    public Profile incrementCount(Profile profile) {
        profile.setTotalInterviewsAttended(profile.getTotalInterviewsAttended()+1);
        return profileRepository.save(profile);
    }
    

    public WorkexResponseDTO updateWorkex(UUID id, WorkexDTO dto, String email) {
        workexValidation(dto);
        WorkExperience workex = workExperienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work experience doesn't exist"));
        workex.setCompanyName(dto.companyName());
        workex.setStartDate(dto.startDate());
        workex.setEndDate(dto.endDate());
        workExperienceRepository.save(workex);

        Profile profile = getProfileUsingEmail(email);

        return new WorkexResponseDTO(profile.getWorkExperiences().stream().map(ele -> getWorkexDto(ele)).toList());

    }

    public List<ProfileResponseDTO> getInterviewers(List<String> skills, int pageNumber, int pageSize) {
        List<String> normalisedSkills = normaliseSkills(skills);
        Pageable page = getRatingSortedPageObject(pageNumber, pageSize);
        Page<Profile> profiles = profileRepository.findInterviewersBySkills(normalisedSkills, page);
        return profiles.get().map((ele) -> getResponseDto(ele)).toList();

    }

  public List<Skill> getAllSkills(List<String> skills) {

    List<String> normalizedSkills = normaliseSkills(skills);

    // Existing skills
    List<Skill> existingSkills =
            skillsRepository.findBySkillNameIn(normalizedSkills);

    Set<String> existingSkillNames = existingSkills.stream()
            .map(Skill::getSkillName)
            .collect(Collectors.toSet());

    // Create missing skills
    List<Skill> newSkills = normalizedSkills.stream()
            .filter(skill -> !existingSkillNames.contains(skill))
            .map(skill -> {
                Skill newSkill = new Skill();
                newSkill.setSkillId(UUID.randomUUID());
                newSkill.setSkillName(skill);
                return newSkill;
            })
            .toList();

    if (!newSkills.isEmpty()) {
        skillsRepository.saveAll(newSkills);
    }

    // Return all skills (existing + newly created)
    List<Skill> allSkills = new ArrayList<>(existingSkills);
    allSkills.addAll(newSkills);

    return allSkills;
}

    @Transactional
    public List<WorkexDTO> getWorkExperiences(String userEmail) {
        Profile profile = getProfileUsingEmail(userEmail) ; 
        List<WorkExperience> workExperiences =  profile.getWorkExperiences() ; 
        return workExperiences.stream().map((ele) -> getWorkexDto(ele)).toList() ; 
    }

    

    
    private boolean validteOwnership(UUID workexId, String email) {
     
        Profile profile = getProfileUsingEmail(email);
        List<WorkExperience> workExperiences = profile.getWorkExperiences() ; 
        List<WorkExperience> filteredWorkExperiences = workExperiences.stream().filter((ele) -> ele.getWorkExperienceId().equals(workexId)).toList() ; 
        return (filteredWorkExperiences.size()!=0 ) ; 

    }

    private void workexValidation(WorkexCreationDTO dto) {

        if (dto.endDate() != null && dto.endDate().isBefore(dto.startDate())) {
            throw new InvalidExperienceException("Invalid experiences shared please check");
        }

    }
    private void workexValidation(WorkexDTO dto) {

        if (dto.endDate() != null && dto.endDate().isBefore(dto.startDate())) {
            throw new InvalidExperienceException("Invalid experiences shared please check");
        }

    }

    private WorkexDTO getWorkexDto(WorkExperience workExperience) {
        return new WorkexDTO(workExperience.getWorkExperienceId(), workExperience.getCompanyName(),
                workExperience.getStartDate(), workExperience.getEndDate());
    }

    public Profile getProfileUsingEmail(String email) {
        return profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("CHECK THE DETAILS PROPERLY PMS"));
    }

    public ProfileResponseDTO getProfileByEmail(String email) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("CHECK THE DETAILS PROPERLY PMS"));
        return getResponseDto(profile) ; 
    }

    private ProfileResponseDTO getResponseDto(Profile profile) {
        return new ProfileResponseDTO(profile.getUser().getEmail(), profile.getUser().getName(),
                profile.getUser().getPhoneNumber(),
                profile.getUser().getUserRole(), profile.getUser().getUserStatus(), profile.getRating(),
                profile.getSkills().stream().map(ele -> ele.getSkillName()).toList());

    }

    private ProfileSkillsDTO getDto(List<Skill> skills) {
        return new ProfileSkillsDTO(skills.stream().map((ele) -> ele.getSkillName()).toList());
    }

    private List<String> normaliseSkills(List<String> skills) {
        return skills.stream()
                .map(skill -> skill.trim().toLowerCase())
                .filter(skill -> !skill.isBlank())
                .distinct()
                .toList();

    }

    private Pageable getRatingSortedPageObject(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize, Sort.by("rating").descending());
    }

}

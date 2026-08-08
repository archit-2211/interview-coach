package com.interviewcoach.project.ProfileManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.interviewcoach.project.GlobalExceptions.UnauthorisedException;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileResponseDTO;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileSkillsDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexCreationDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexResponseDTO;
import com.interviewcoach.project.ProfileManagement.exceptions.InvalidExperienceException;
import com.interviewcoach.project.ProfileManagement.exceptions.UserNotFoundException;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Skill;
import com.interviewcoach.project.models.User;
import com.interviewcoach.project.models.WorkExperience;

@ExtendWith(MockitoExtension.class)
class ProfileManagementServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private SkillsRepository skillsRepository;

    @Mock
    private WorkExperienceRepository workExperienceRepository;

    @InjectMocks
    private ProfileManagementService pmService;


    // =========================================================
    // createProfile()
    // =========================================================

    @Test
    void createProfile_success() {

        User user = new User();
        user.setEmail("candidate@gmail.com");

        Profile savedProfile = new Profile();
        savedProfile.setUser(user);

        when(profileRepository.save(any(Profile.class)))
                .thenReturn(savedProfile);

        Profile result = pmService.createProfile(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());

        verify(profileRepository).save(any(Profile.class));
    }


    // =========================================================
    // getProfile()
    // =========================================================

    @Test
    void getProfile_success() {

        String email = "candidate@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setName("Archit");

        Profile profile = new Profile();
        profile.setUser(user);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        ProfileResponseDTO result =
                pmService.getProfile(email);

        assertNotNull(result);
        assertEquals(email, result.email());
        assertEquals("Archit", result.name());

        verify(profileRepository)
                .findByUserEmail(email);
    }


    @Test
    void getProfile_profileNotFound() {

        String email = "candidate@gmail.com";

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> pmService.getProfile(email)
        );

        verify(profileRepository)
                .findByUserEmail(email);
    }


    // =========================================================
    // updateSkills()
    // =========================================================

    @Test
    void updateSkills_existingAndNewSkills() {

        String email = "candidate@gmail.com";

        User user = new User();
        user.setEmail(email);

        Profile profile = new Profile();
        profile.setUser(user);

        Skill java = new Skill();
        java.setSkillName("java");

        Skill spring = new Skill();
        spring.setSkillName("spring");

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(skillsRepository.findBySkillNameIn(
                List.of("java", "spring")))
                .thenReturn(List.of(java));

        when(skillsRepository.saveAll(anyList()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(profileRepository.save(profile))
                .thenReturn(profile);

        ProfileSkillsDTO result =
                pmService.updateSkills(
                        List.of(" Java ", "SPRING"),
                        email
                );

        assertNotNull(result);

        assertEquals(
                List.of("java", "spring"),
                result.skills()
        );

        assertEquals(
                2,
                profile.getSkills().size()
        );

        verify(skillsRepository)
                .findBySkillNameIn(
                        List.of("java", "spring")
                );

        verify(skillsRepository)
                .saveAll(anyList());

        verify(profileRepository)
                .save(profile);
    }


    @Test
    void updateSkills_allSkillsAlreadyExist() {

        String email = "candidate@gmail.com";

        User user = new User();
        user.setEmail(email);

        Profile profile = new Profile();
        profile.setUser(user);

        Skill java = new Skill();
        java.setSkillName("java");

        Skill spring = new Skill();
        spring.setSkillName("spring");

        List<Skill> existingSkills =
                List.of(java, spring);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(skillsRepository.findBySkillNameIn(
                List.of("java", "spring")))
                .thenReturn(existingSkills);

        when(skillsRepository.saveAll(anyList()))
                .thenReturn(List.of());

        when(profileRepository.save(profile))
                .thenReturn(profile);

        ProfileSkillsDTO result =
                pmService.updateSkills(
                        List.of("Java", "Spring"),
                        email
                );

        assertEquals(
                List.of("java", "spring"),
                result.skills()
        );

        assertEquals(
                2,
                profile.getSkills().size()
        );

        assertTrue(profile.getSkills().contains(java));
        assertTrue(profile.getSkills().contains(spring));

        verify(skillsRepository)
                .findBySkillNameIn(
                        List.of("java", "spring")
                );

        // Your current production code calls saveAll()
        // even when newSkills is empty.
        verify(skillsRepository)
                .saveAll(anyList());

        verify(profileRepository)
                .save(profile);
    }


    @Test
    void updateSkills_normalisesAndRemovesDuplicates() {

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(skillsRepository.findBySkillNameIn(
                List.of("java", "spring")))
                .thenReturn(List.of());

        when(skillsRepository.saveAll(anyList()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        when(profileRepository.save(profile))
                .thenReturn(profile);

        pmService.updateSkills(
                List.of(
                        " Java ",
                        "JAVA",
                        " ",
                        "Spring"
                ),
                email
        );

        verify(skillsRepository)
                .findBySkillNameIn(
                        List.of("java", "spring")
                );
    }


    // =========================================================
    // removeSkillFromProfile()
    // =========================================================

    @Test
    void removeSkillFromProfile_success() {

        String email = "candidate@gmail.com";

        User user = new User();
        user.setEmail(email);

        Profile profile = new Profile();
        profile.setUser(user);

        Skill java = new Skill();
        java.setSkillName("java");

        Skill spring = new Skill();
        spring.setSkillName("spring");

        profile.getSkills().add(java);
        profile.getSkills().add(spring);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(profileRepository.save(profile))
                .thenReturn(profile);

        ProfileResponseDTO result =
                pmService.removeSkillFromProfile(
                        List.of("Java"),
                        email
                );

        assertNotNull(result);

        assertEquals(
                1,
                profile.getSkills().size()
        );

        assertEquals(
                "spring",
                profile.getSkills()
                        .get(0)
                        .getSkillName()
        );

        verify(profileRepository)
                .save(profile);
    }


    @Test
    void removeSkillFromProfile_skillDoesNotExist() {

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        Skill java = new Skill();
        java.setSkillName("java");

        profile.getSkills().add(java);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(profileRepository.save(profile))
                .thenReturn(profile);

        pmService.removeSkillFromProfile(
                List.of("python"),
                email
        );

        assertEquals(
                1,
                profile.getSkills().size()
        );

        assertEquals(
                "java",
                profile.getSkills()
                        .get(0)
                        .getSkillName()
        );

        verify(profileRepository)
                .save(profile);
    }


    // =========================================================
    // addWorkex()
    // =========================================================

    @Test
    void addWorkex_success() {

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        WorkexCreationDTO dto =
                new WorkexCreationDTO(
                        "Google",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1)
                );

        WorkExperience savedWorkex =
                WorkExperience.builder()
                        .workExperienceId(UUID.randomUUID())
                        .companyName(dto.companyName())
                        .startDate(dto.startDate())
                        .endDate(dto.endDate())
                        .profile(profile)
                        .build();

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(workExperienceRepository.save(
                any(WorkExperience.class)))
                .thenReturn(savedWorkex);

        /*
         * addWorkex() calls getWorkExperiences(email)
         * after saving the work experience.
         */
        profile.getWorkExperiences()
                .add(savedWorkex);

        WorkexResponseDTO result =
                pmService.addWorkex(email, dto);

        assertNotNull(result);

        ArgumentCaptor<WorkExperience> captor =
                ArgumentCaptor.forClass(
                        WorkExperience.class
                );

        verify(workExperienceRepository)
                .save(captor.capture());

        WorkExperience captured =
                captor.getValue();

        assertEquals(
                "Google",
                captured.getCompanyName()
        );

        assertEquals(
                dto.startDate(),
                captured.getStartDate()
        );

        assertEquals(
                dto.endDate(),
                captured.getEndDate()
        );

        assertEquals(
                profile,
                captured.getProfile()
        );
    }


    @Test
    void addWorkex_invalidDates() {

        String email = "candidate@gmail.com";

        WorkexCreationDTO dto =
                new WorkexCreationDTO(
                        "Google",
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2022, 1, 1)
                );

        assertThrows(
                InvalidExperienceException.class,
                () -> pmService.addWorkex(
                        email,
                        dto
                )
        );

        verify(workExperienceRepository, never())
                .save(any());
    }


    // =========================================================
    // deleteWorkex()
    // =========================================================

    @Test
    void deleteWorkex_success() {

        UUID workexId = UUID.randomUUID();

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        WorkExperience workex =
                new WorkExperience();

        workex.setWorkExperienceId(workexId);

        profile.getWorkExperiences()
                .add(workex);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        pmService.deleteWorkex(
                workexId,
                email
        );

        verify(workExperienceRepository)
                .deleteWorkExperienceById(workexId);
    }


    @Test
    void deleteWorkex_notOwner() {

        UUID workexId = UUID.randomUUID();

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        assertThrows(
                UnauthorisedException.class,
                () -> pmService.deleteWorkex(
                        workexId,
                        email
                )
        );

        verify(workExperienceRepository, never())
                .deleteWorkExperienceById(any());
    }


    // =========================================================
    // incrementCount()
    // =========================================================

    @Test
    void incrementCount_success() {

        Profile profile = new Profile();

        profile.setTotalInterviewsAttended(5);

        when(profileRepository.save(profile))
                .thenReturn(profile);

        Profile result =
                pmService.incrementCount(profile);

        assertEquals(
                6,
                result.getTotalInterviewsAttended()
        );

        verify(profileRepository)
                .save(profile);
    }


    // =========================================================
    // updateWorkex()
    // =========================================================

    @Test
    void updateWorkex_success() {

        UUID workexId = UUID.randomUUID();

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        WorkExperience workex =
                new WorkExperience();

        workex.setWorkExperienceId(workexId);
        workex.setCompanyName("Old Company");
        workex.setStartDate(
                LocalDate.of(2020, 1, 1)
        );
        workex.setEndDate(
                LocalDate.of(2022, 1, 1)
        );

        profile.getWorkExperiences()
                .add(workex);

        WorkexDTO dto =
                new WorkexDTO(
                        workexId,
                        "New Company",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1)
                );

        when(workExperienceRepository.findById(workexId))
                .thenReturn(Optional.of(workex));

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        when(workExperienceRepository.save(workex))
                .thenReturn(workex);

        WorkexResponseDTO result =
                pmService.updateWorkex(
                        workexId,
                        dto,
                        email
                );

        assertNotNull(result);

        assertEquals(
                "New Company",
                workex.getCompanyName()
        );

        assertEquals(
                dto.startDate(),
                workex.getStartDate()
        );

        assertEquals(
                dto.endDate(),
                workex.getEndDate()
        );

        verify(workExperienceRepository)
                .findById(workexId);

        verify(workExperienceRepository)
                .save(workex);

        verify(profileRepository)
                .findByUserEmail(email);
    }


    @Test
    void updateWorkex_notFound() {

        UUID workexId = UUID.randomUUID();

        WorkexDTO dto =
                new WorkexDTO(
                        workexId,
                        "Google",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1)
                );

        when(workExperienceRepository.findById(workexId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> pmService.updateWorkex(
                                workexId,
                                dto,
                                "candidate@gmail.com"
                        )
                );

        assertEquals(
                "Work experience doesn't exist",
                exception.getMessage()
        );

        verify(workExperienceRepository, never())
                .save(any());
    }


    @Test
    void updateWorkex_invalidDates() {

        UUID workexId = UUID.randomUUID();

        WorkexDTO dto =
                new WorkexDTO(
                        workexId,
                        "Google",
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2022, 1, 1)
                );

        assertThrows(
                InvalidExperienceException.class,
                () -> pmService.updateWorkex(
                        workexId,
                        dto,
                        "candidate@gmail.com"
                )
        );

        verify(workExperienceRepository, never())
                .findById(any());
    }


    // =========================================================
    // getInterviewers()
    // =========================================================

    @Test
    void getInterviewers_success() {

        List<String> skills =
                List.of("Java", "Spring");

        Profile profile = new Profile();

        User user = new User();
        user.setEmail("interviewer@gmail.com");
        user.setName("Interviewer");

        profile.setUser(user);

        Skill java = new Skill();
        java.setSkillName("java");

        profile.getSkills().add(java);

        Page<Profile> page =
                new PageImpl<>(
                        List.of(profile)
                );

        when(profileRepository.findInterviewersBySkills(
                eq(List.of("java", "spring")),
                any(Pageable.class)))
                .thenReturn(page);

        List<ProfileResponseDTO> result =
                pmService.getInterviewers(
                        skills,
                        0,
                        10
                );

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "interviewer@gmail.com",
                result.get(0).email()
        );

        assertEquals(
                "Interviewer",
                result.get(0).name()
        );

        assertEquals(
                List.of("java"),
                result.get(0).skills()
        );

        verify(profileRepository)
                .findInterviewersBySkills(
                        eq(List.of("java", "spring")),
                        any(Pageable.class)
                );
    }


    @Test
    void getInterviewers_emptyResult() {

        Page<Profile> page =
                new PageImpl<>(
                        List.of()
                );

        when(profileRepository.findInterviewersBySkills(
                eq(List.of("java")),
                any(Pageable.class)))
                .thenReturn(page);

        List<ProfileResponseDTO> result =
                pmService.getInterviewers(
                        List.of("Java"),
                        0,
                        10
                );

        assertTrue(result.isEmpty());

        verify(profileRepository)
                .findInterviewersBySkills(
                        eq(List.of("java")),
                        any(Pageable.class)
                );
    }


    // =========================================================
    // getAllSkills()
    // =========================================================

    @Test
    void getAllSkills_existingAndNewSkills() {

        Skill java = new Skill();
        java.setSkillName("java");

        when(skillsRepository.findBySkillNameIn(
                List.of("java", "spring")))
                .thenReturn(List.of(java));

        when(skillsRepository.saveAll(anyList()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        List<Skill> result =
                pmService.getAllSkills(
                        List.of("Java", "Spring")
                );

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                "java",
                result.get(0).getSkillName()
        );

        assertEquals(
                "spring",
                result.get(1).getSkillName()
        );

        verify(skillsRepository)
                .findBySkillNameIn(
                        List.of("java", "spring")
                );

        verify(skillsRepository)
                .saveAll(anyList());
    }


    @Test
    void getAllSkills_allSkillsAlreadyExist() {

        Skill java = new Skill();
        java.setSkillName("java");

        Skill spring = new Skill();
        spring.setSkillName("spring");

        when(skillsRepository.findBySkillNameIn(
                List.of("java", "spring")))
                .thenReturn(
                        List.of(java, spring)
                );

        List<Skill> result =
                pmService.getAllSkills(
                        List.of("Java", "Spring")
                );

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                "java",
                result.get(0).getSkillName()
        );

        assertEquals(
                "spring",
                result.get(1).getSkillName()
        );

        verify(skillsRepository, never())
                .saveAll(anyList());
    }


    // =========================================================
    // getWorkExperiences()
    // =========================================================

    @Test
    void getWorkExperiences_success() {

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        UUID workexId = UUID.randomUUID();

        WorkExperience workex =
                new WorkExperience();

        workex.setWorkExperienceId(workexId);
        workex.setCompanyName("Google");
        workex.setStartDate(
                LocalDate.of(2022, 1, 1)
        );
        workex.setEndDate(
                LocalDate.of(2025, 1, 1)
        );

        profile.getWorkExperiences()
                .add(workex);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        List<WorkexDTO> result =
                pmService.getWorkExperiences(email);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                workexId,
                result.get(0).id()
        );

        assertEquals(
                "Google",
                result.get(0).companyName()
        );

        assertEquals(
                LocalDate.of(2022, 1, 1),
                result.get(0).startDate()
        );

        assertEquals(
                LocalDate.of(2025, 1, 1),
                result.get(0).endDate()
        );

        verify(profileRepository)
                .findByUserEmail(email);
    }


    // =========================================================
    // getProfileUsingEmail()
    // =========================================================

    @Test
    void getProfileUsingEmail_success() {

        String email = "candidate@gmail.com";

        Profile profile = new Profile();

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        Profile result =
                pmService.getProfileUsingEmail(email);

        assertEquals(
                profile,
                result
        );

        verify(profileRepository)
                .findByUserEmail(email);
    }


    @Test
    void getProfileUsingEmail_notFound() {

        String email = "candidate@gmail.com";

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> pmService.getProfileUsingEmail(email)
        );

        verify(profileRepository)
                .findByUserEmail(email);
    }


    // =========================================================
    // getProfileByEmail()
    // =========================================================

    @Test
    void getProfileByEmail_success() {

        String email = "candidate@gmail.com";

        User user = new User();
        user.setEmail(email);
        user.setName("Archit");

        Profile profile = new Profile();
        profile.setUser(user);

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.of(profile));

        ProfileResponseDTO result =
                pmService.getProfileByEmail(email);

        assertNotNull(result);

        assertEquals(
                email,
                result.email()
        );

        assertEquals(
                "Archit",
                result.name()
        );

        verify(profileRepository)
                .findByUserEmail(email);
    }


    @Test
    void getProfileByEmail_notFound() {

        String email = "candidate@gmail.com";

        when(profileRepository.findByUserEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> pmService.getProfileByEmail(email)
        );

        verify(profileRepository)
                .findByUserEmail(email);
    }
}
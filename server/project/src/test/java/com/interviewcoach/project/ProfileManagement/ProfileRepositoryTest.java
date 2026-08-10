package com.interviewcoach.project.ProfileManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.interviewcoach.project.auth.UserRepository;
import com.interviewcoach.project.enums.AuthenticationSource;
import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Skill;
import com.interviewcoach.project.models.User;

@DataJpaTest
public class ProfileRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SkillsRepository skillsRepository;
    @Autowired
    ProfileRepository profileRepository;

    private Profile createInterviewer(
            String email,
            double rating,
            Skill skill) {

        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setEmail(email);
        user.setAuthenticationSource(AuthenticationSource.LOCAL);
        user.setUserRole(UserRole.INTERVIEWER);

        userRepository.save(user);

        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID());
        profile.setUser(user);
        profile.setRating(rating);
        profile.getSkills().add(skill);

        return profileRepository.save(profile);
    }

    @Test
    void findInterviewersBySkills_success() {

        User interviewerUser = new User();
        interviewerUser.setUserId(UUID.randomUUID());
        interviewerUser.setEmail("interviewer@gmail.com");
        interviewerUser.setAuthenticationSource(AuthenticationSource.LOCAL);
        interviewerUser.setUserRole(UserRole.INTERVIEWER);

        userRepository.save(interviewerUser);

        Skill java = new Skill();
        java.setSkillId(UUID.randomUUID());
        java.setSkillName("java");

        skillsRepository.save(java);

        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID());
        profile.setUser(interviewerUser);
        profile.setRating(4.5);
        profile.getSkills().add(java);

        profileRepository.save(profile);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("rating").descending());

        Page<Profile> result = profileRepository.findInterviewersBySkills(
                List.of("java"),
                pageable);

        assertEquals(1, result.getTotalElements());

        assertEquals(
                profile.getProfileId(),
                result.getContent().get(0).getProfileId());
    }

    @Test
    void findInterviewersBySkills_excludesCandidate() {

        User candidate = new User();
        candidate.setUserId(UUID.randomUUID());
        candidate.setEmail("candidate@gmail.com");
        candidate.setAuthenticationSource(AuthenticationSource.LOCAL);
        candidate.setUserRole(UserRole.CANDIDATE);

        userRepository.save(candidate);

        Skill java = new Skill();
        java.setSkillId(UUID.randomUUID());
        java.setSkillName("java");

        skillsRepository.save(java);

        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID());
        profile.setUser(candidate);
        profile.setRating(5.0);
        profile.getSkills().add(java);

        profileRepository.save(profile);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Profile> result = profileRepository.findInterviewersBySkills(
                List.of("java"),
                pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void findInterviewersBySkills_sortedByRatingAndPaginated() {

        Skill java = new Skill();
        java.setSkillId(UUID.randomUUID());
        java.setSkillName("java");

        skillsRepository.save(java);

        Profile low = createInterviewer(
                "low@gmail.com",
                3.0,
                java);

        Profile high = createInterviewer(
                "high@gmail.com",
                5.0,
                java);

        Profile medium = createInterviewer(
                "medium@gmail.com",
                4.0,
                java);

        Pageable pageable = PageRequest.of(
                0,
                2,
                Sort.by("rating").descending());

        Page<Profile> result = profileRepository.findInterviewersBySkills(
                List.of("java"),
                pageable);

        assertEquals(3, result.getTotalElements());

        assertEquals(2, result.getContent().size());

        assertEquals(
                high.getProfileId(),
                result.getContent().get(0).getProfileId());

        assertEquals(
                medium.getProfileId(),
                result.getContent().get(1).getProfileId());
    }

    @Test
    void findInterviewersBySkills_noMatchingSkill() {
        Skill java = new Skill();
        java.setSkillId(UUID.randomUUID());
        java.setSkillName("java");

        skillsRepository.save(java);

        Profile profile = createInterviewer(
                "interviewer@gmail.com",
                4.5,
                java);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Profile> result = profileRepository.findInterviewersBySkills(
                List.of("python"),
                pageable);

        assertTrue(result.isEmpty());
    }

}

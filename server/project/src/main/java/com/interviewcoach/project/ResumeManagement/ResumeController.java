package com.interviewcoach.project.ResumeManagement;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interviewcoach.project.ResumeManagement.dto.ResumeResponseDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(
            ResumeService resumeService
    ) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeResponseDTO> uploadResume(
            @RequestParam("file")
            MultipartFile file
    ) {

        return ResponseEntity.ok(
                resumeService.uploadResume(
                        file,
                        getEmail()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponseDTO>> getMyResumes() {

        return ResponseEntity.ok(
                resumeService.getMyResumes(
                        getEmail()
                )
        );
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable UUID resumeId
    ) {

        resumeService.deleteResume(
                resumeId,
                getEmail()
        );

        return ResponseEntity.noContent()
                .build();
    }

    private String getEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
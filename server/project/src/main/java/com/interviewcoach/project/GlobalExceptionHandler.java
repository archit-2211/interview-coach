package com.interviewcoach.project;


import java.time.LocalDateTime;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.interviewcoach.project.GlobalExceptions.UnauthorisedException;
import com.interviewcoach.project.ProfileManagement.exceptions.EmptyFileException;
import com.interviewcoach.project.ProfileManagement.exceptions.InvalidExperienceException;
import com.interviewcoach.project.ProfileManagement.exceptions.InvalidFileTypeException;
import com.interviewcoach.project.ProfileManagement.exceptions.UserNotFoundException;
import com.interviewcoach.project.ResumeManagement.exceptions.ProfileNotFoundException;
import com.interviewcoach.project.SlotManagement.exceptions.InvalidSlotException;
import com.interviewcoach.project.SlotManagement.exceptions.SlotNotFoundException;
import com.interviewcoach.project.SlotManagement.exceptions.SlotUnavailableException;
import com.interviewcoach.project.auth.dto.ApiError;
import com.interviewcoach.project.auth.exceptions.IncorrectCredentialsException;
import com.interviewcoach.project.auth.exceptions.InvalidRefreshTokenException;
import com.interviewcoach.project.auth.exceptions.UserExistsException;
import com.interviewcoach.project.auth.exceptions.OAuthException;
import com.interviewcoach.project.auth.exceptions.UnverifiedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(IncorrectCredentialsException.class)
        public ResponseEntity<ApiError> handleIncorrectCredentials(
                        IncorrectCredentialsException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.UNAUTHORIZED.value(),
                                                                LocalDateTime.now()));
        }

        @ExceptionHandler(InvalidRefreshTokenException.class)
        public ResponseEntity<ApiError> handleInvalidRefreshToken(
                        InvalidRefreshTokenException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.UNAUTHORIZED.value(),
                                                                LocalDateTime.now()));
        }

        @ExceptionHandler(OAuthException.class)
        public ResponseEntity<ApiError> handleOAuthException(
                        OAuthException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.UNAUTHORIZED.value(),
                                                                LocalDateTime.now()));
        }

        @ExceptionHandler(UserExistsException.class)
        public ResponseEntity<ApiError> handleUserExists(
                        UserExistsException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.CONFLICT.value(),
                                                                LocalDateTime.now()));
        }
        @ExceptionHandler(UnverifiedException.class)
        public ResponseEntity<ApiError> handleUnverifiedException(
                        UnverifiedException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                "User is not yet verified please verify first",
                                                                HttpStatus.CONFLICT.value(),
                                                                LocalDateTime.now()));
        }
        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ApiError> handleUserNotFound(
                        UserNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }

        @ExceptionHandler(InvalidExperienceException.class)
        public ResponseEntity<ApiError> handleInvalidExperience(
                        InvalidExperienceException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }
        @ExceptionHandler(UnauthorisedException.class)
        public ResponseEntity<ApiError> handleUnauthorisedResumeRequest(
                        UnauthorisedException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }
        

        @ExceptionHandler(ProfileNotFoundException.class)
        public ResponseEntity<ApiError> handleUProfileNotFound(
                        ProfileNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }
        @ExceptionHandler(InvalidFileTypeException.class)
        public ResponseEntity<ApiError> handleInvalidFileType(
                        InvalidFileTypeException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.CONFLICT.value(),
                                                                LocalDateTime.now()));
        }
         @ExceptionHandler(EmptyFileException.class)
        public ResponseEntity<ApiError> handleEmptyFile(
                        EmptyFileException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.CONFLICT.value(),
                                                                LocalDateTime.now()));
        }

         @ExceptionHandler(SlotNotFoundException.class)
        public ResponseEntity<ApiError> handleInvalidSlot(
                        SlotNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }
         @ExceptionHandler(SlotUnavailableException.class)
        public ResponseEntity<ApiError> handleInvalidSlot(
                        SlotUnavailableException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.CONFLICT.value(),
                                                                LocalDateTime.now()));
        }

         @ExceptionHandler(InvalidSlotException.class)
        public ResponseEntity<ApiError> handleInvalidSlot(
                        InvalidSlotException ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiError> handleRuntimeException(
                        RuntimeException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }

       

        @ExceptionHandler(EmptyResultDataAccessException.class) 
        public ResponseEntity<ApiError> handleERDException(
                        EmptyResultDataAccessException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(
                                                new ApiError(
                                                                ex.getMessage(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                LocalDateTime.now()));
        }




        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<ApiError> handleException(
        //                 Exception ex) {

        //         return ResponseEntity
        //                         .status(HttpStatus.INTERNAL_SERVER_ERROR)
        //                         .body(
        //                                         new ApiError(
        //                                                         "Something went wrong",
        //                                                         HttpStatus.INTERNAL_SERVER_ERROR.value(),
        //                                                         LocalDateTime.now()));
        // }
}
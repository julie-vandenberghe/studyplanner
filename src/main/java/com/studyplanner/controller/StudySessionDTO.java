package com.studyplanner.controller;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudySessionDTO {


    private String studentName;

    @NotBlank(message = "Le sujet est obligatoire")
    private String subject;

    @NotBlank(message = "La description est obligatoire")
    private String description;


    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime startTime;


    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime endTime;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    
}

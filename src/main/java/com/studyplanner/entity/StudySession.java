package com.studyplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "study_sessions")
public class StudySession {

    // Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    @NotBlank(message = "Le nom de l'étudiant est obligatoire")
    private String studentName;
    @Column(nullable = false)
    @NotBlank(message = "Le sujet est obligatoire")
    private String subject;

    private String description;

    @Column(nullable = false)
    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime endTime;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public StudySession(LocalDateTime endTime, LocalDateTime startTime, String description, String subject, String studentName) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.description = description;
        this.subject = subject;
        this.studentName = studentName;
    }

    // Gestion des timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }



}

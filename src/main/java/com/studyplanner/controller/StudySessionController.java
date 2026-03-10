package com.studyplanner.controller;

import com.studyplanner.entity.StudySession;
import com.studyplanner.service.StudySessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class StudySessionController {

    private final StudySessionService studySessionService;

    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @PostMapping
    public ResponseEntity<StudySession> createStudySession(@Valid @RequestBody StudySession session, Principal principal) {
        StudySession createdSession = studySessionService.createStudySession(session, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    @GetMapping
    public ResponseEntity<List<StudySession>> getStudySessions(Principal principal) {
        List<StudySession> sessions = studySessionService.getStudySessions(principal.getName());
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudySession> getStudySessionById(@PathVariable Long id, Principal principal) {
        StudySession session = studySessionService.getStudySessionById(id, principal.getName());
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudySession> updateStudySession(
            @PathVariable Long id,
            @Valid @RequestBody StudySession session,
            Principal principal
    ) {
        StudySession updatedSession = studySessionService.updateStudySession(id, session, principal.getName());
        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudySession(@PathVariable Long id, Principal principal) {
        studySessionService.deleteStudySession(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}

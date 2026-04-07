package com.studyplanner.controller;

import com.studyplanner.entity.StudySession;
import com.studyplanner.service.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Study Sessions", description = "Gestion des sessions de revision")
@SecurityRequirement(name = "basicAuth")
public class StudySessionController {

    private final StudySessionService studySessionService;

    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    
    @PostMapping
        @Operation(summary = "Creer une session", description = "Cree une nouvelle session de revision pour l'utilisateur connecte")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session creee",
                content = @Content(schema = @Schema(implementation = StudySession.class))),
            @ApiResponse(responseCode = "400", description = "Donnees invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifie")
        })
    public ResponseEntity<StudySession> createStudySession(@Valid @RequestBody StudySessionDTO session, Principal principal) {
        StudySession studySession = new StudySession(session.getEndTime(),session.getStartTime(), session.getDescription(), session.getSubject(), session.getStudentName());
        StudySession createdSession = studySessionService.createStudySession(studySession, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }

    @GetMapping
        @Operation(summary = "Lister les sessions", description = "Retourne les sessions de revision de l'utilisateur connecte")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste retournee"),
            @ApiResponse(responseCode = "401", description = "Non authentifie")
        })
    public ResponseEntity<List<StudySession>> getStudySessions(Principal principal) {
        List<StudySession> sessions = studySessionService.getStudySessions(principal.getName());
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
        @Operation(summary = "Recuperer une session", description = "Retourne une session par son identifiant")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session trouvee"),
            @ApiResponse(responseCode = "404", description = "Session introuvable"),
            @ApiResponse(responseCode = "401", description = "Non authentifie")
        })
        public ResponseEntity<StudySession> getStudySessionById(@Parameter(description = "Identifiant de la session") @PathVariable Long id, Principal principal) {
        StudySession session = studySessionService.getStudySessionById(id, principal.getName());
        return ResponseEntity.ok(session);
    }

    @PutMapping("/{id}")
        @Operation(summary = "Mettre a jour une session", description = "Met a jour une session existante")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session mise a jour"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides"),
            @ApiResponse(responseCode = "404", description = "Session introuvable"),
            @ApiResponse(responseCode = "401", description = "Non authentifie")
        })
    public ResponseEntity<StudySession> updateStudySession(
            @Parameter(description = "Identifiant de la session") @PathVariable Long id,
            @Valid @RequestBody StudySession session,
            Principal principal
    ) {
        StudySession updatedSession = studySessionService.updateStudySession(id, session, principal.getName());
        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/{id}")
        @Operation(summary = "Supprimer une session", description = "Supprime une session par son identifiant")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Session supprimee"),
            @ApiResponse(responseCode = "404", description = "Session introuvable"),
            @ApiResponse(responseCode = "401", description = "Non authentifie")
        })
        public ResponseEntity<Void> deleteStudySession(@Parameter(description = "Identifiant de la session") @PathVariable Long id, Principal principal) {
        studySessionService.deleteStudySession(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}

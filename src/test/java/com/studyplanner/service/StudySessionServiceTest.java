package com.studyplanner.service;

import com.studyplanner.entity.StudySession;
import com.studyplanner.exception.BusinessException;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.repository.StudySessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudySessionServiceTest {

    @Mock
    private StudySessionRepository studySessionRepository;

    @InjectMocks
    private StudySessionService studySessionService;

    @Test
    void shouldCreateStudySessionSuccessfully() {
        String user = "alice";
        StudySession sessionToCreate = buildValidSession("ignored-user");

        when(studySessionRepository.save(any(StudySession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudySession created = studySessionService.createStudySession(sessionToCreate, user);

        assertEquals(user, created.getStudentName());
        assertEquals("Maths", created.getSubject());
        verify(studySessionRepository).save(sessionToCreate);
    }

    @Test
    void shouldRejectSessionWhenEndTimeIsBeforeStartTime() {
        StudySession invalidSession = buildValidSession("alice");
        invalidSession.setStartTime(LocalDateTime.now().plusDays(2));
        invalidSession.setEndTime(LocalDateTime.now().plusDays(1));

        assertThrows(BusinessException.class,
                () -> studySessionService.createStudySession(invalidSession, "alice"));

        verify(studySessionRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenSessionDoesNotExist() {
        when(studySessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> studySessionService.getStudySessionById(99L, "alice"));
    }

    @Test
    void shouldThrowWhenAccessingAnotherUsersSession() {
        StudySession session = buildValidSession("bob");
        session.setId(10L);
        when(studySessionRepository.findById(10L)).thenReturn(Optional.of(session));

        assertThrows(ResourceNotFoundException.class,
                () -> studySessionService.getStudySessionById(10L, "alice"));
    }

    @Test
    void shouldReturnOnlyCurrentUsersStudySessions() {
        String user = "alice";
        List<StudySession> expectedSessions = List.of(
                buildValidSession(user),
                buildValidSession(user)
        );
        when(studySessionRepository.findByStudentName(user)).thenReturn(expectedSessions);

        List<StudySession> result = studySessionService.getStudySessions(user);

        assertEquals(2, result.size());
        assertSame(expectedSessions, result);
        verify(studySessionRepository).findByStudentName(user);
    }

    @Test
    void shouldUpdateOwnedSessionSuccessfully() {
        String user = "alice";
        StudySession existingSession = buildValidSession(user);
        existingSession.setId(1L);

        StudySession updatedSession = buildValidSession("ignored-user");
        updatedSession.setSubject("Physique");
        updatedSession.setDescription("Exercices de dynamique");
        updatedSession.setStartTime(LocalDateTime.now().plusDays(2));
        updatedSession.setEndTime(LocalDateTime.now().plusDays(2).plusHours(3));

        when(studySessionRepository.findById(1L)).thenReturn(Optional.of(existingSession));
        when(studySessionRepository.save(existingSession)).thenReturn(existingSession);

        StudySession result = studySessionService.updateStudySession(1L, updatedSession, user);

        assertEquals("Physique", result.getSubject());
        assertEquals("Exercices de dynamique", result.getDescription());
        assertEquals(updatedSession.getStartTime(), result.getStartTime());
        assertEquals(updatedSession.getEndTime(), result.getEndTime());
        assertEquals(user, result.getStudentName());
        verify(studySessionRepository).findById(1L);
        verify(studySessionRepository).save(existingSession);
    }

    private StudySession buildValidSession(String owner) {
        StudySession session = new StudySession();
        session.setStudentName(owner);
        session.setSubject("Maths");
        session.setDescription("Révisions chapitres 1 à 3");
        session.setStartTime(LocalDateTime.now().plusDays(1));
        session.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        return session;
    }
}
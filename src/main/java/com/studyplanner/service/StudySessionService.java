package com.studyplanner.service;

import com.studyplanner.entity.StudySession;
import com.studyplanner.exception.BusinessException;
import com.studyplanner.exception.ResourceNotFoundException;
import com.studyplanner.repository.StudySessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;

    public StudySessionService(StudySessionRepository studySessionRepository){
        this.studySessionRepository = studySessionRepository;

    }

    public StudySession createStudySession(StudySession session, String user) {
        session.setStudentName(user);
        validateSessionDates(session);
        return studySessionRepository.save(session);
    }

    public List<StudySession> getStudySessions(String user) {
        return studySessionRepository.findByStudentName(user);
    }

    public StudySession getStudySessionById(Long id, String user) {
        return getOwnedStudySession(id, user);
    }

    public StudySession updateStudySession(Long id, StudySession updatedSession, String user) {
        StudySession existingSession = getOwnedStudySession(id, user);
        validateSessionDates(updatedSession);
        existingSession.setSubject(updatedSession.getSubject());
        existingSession.setDescription(updatedSession.getDescription());
        existingSession.setStartTime(updatedSession.getStartTime());
        existingSession.setEndTime(updatedSession.getEndTime());
        return studySessionRepository.save(existingSession);
    }

    public void deleteStudySession(Long id, String user) {
        StudySession session = getOwnedStudySession(id, user);
        studySessionRepository.delete(session);
    }

    private StudySession getOwnedStudySession(Long id, String user) {
        StudySession session = studySessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session introuvable"));

        if (!session.getStudentName().equals(user)) {
            throw new ResourceNotFoundException("Session introuvable");
        }

        return session;
    }

    private void validateSessionDates(StudySession session) {
        if (session.getStartTime() != null && session.getEndTime() != null
                && !session.getEndTime().isAfter(session.getStartTime())) {
            throw new BusinessException("La date de fin doit être après la date de début");
        }
    }
    
}

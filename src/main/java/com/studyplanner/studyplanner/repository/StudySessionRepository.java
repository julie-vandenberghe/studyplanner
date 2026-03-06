package com.studyplanner.studyplanner.repository;

import com.studyplanner.studyplanner.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository pour l'entité StudySession.
 * JpaRepository fournit les opérations CRUD de base :
 * save(), findById(), findAll(), delete()...
 *
 * Spring Data JPA génère automatiquement le SQL à partir du nom des méthodes.
 * Convention : findBy/countBy + nom exact du champ dans l'entité.
 */

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    // récup ttes les sessions d'un étudiant
    List<StudySession> findByStudentName(String studentName);

    // compte le nb de sessions d'un étudiant
    long countByStudentName(String studentName);

}

package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import miage.spring.demo.model.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByTitleconf(String titleconf);

    List<Conference> findByTitleconfContainingIgnoreCase(String titleconf);

    List<Conference> findByOrganizer_Id(Long organizerId);

    List<Conference> findByParticipants_Id(Long userId);

    List<Conference> findByThematiques_IdThematique(Long idThematique);
}

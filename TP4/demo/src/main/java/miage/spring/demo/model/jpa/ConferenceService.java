package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miage.spring.demo.model.Conference;

@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;

    @Autowired
    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public List<Conference> findAll() {
        return conferenceRepository.findAll();
    }

    public List<Conference> findByTitleconf(String titleconf) {
        return conferenceRepository.findByTitleconf(titleconf);
    }

    public List<Conference> findByTitleContaining(String titleconf) {
        return conferenceRepository.findByTitleconfContainingIgnoreCase(titleconf);
    }

    public List<Conference> findByOrganizerId(Long organizerId) {
        return conferenceRepository.findByOrganizer_Id(organizerId);
    }

    public List<Conference> findByParticipantId(Long userId) {
        return conferenceRepository.findByParticipants_Id(userId);
    }

    public List<Conference> findByThematiqueId(Long idThematique) {
        return conferenceRepository.findByThematiques_IdThematique(idThematique);
    }

    public Conference addConference(Conference conference) {
        return conferenceRepository.save(conference);
    }

    public Conference updateConference(Conference conference) {
        mergeWithExistingConference(conference);
        return conferenceRepository.save(conference);
    }

    public void deleteById(Long idconf) {
        conferenceRepository.deleteById(idconf);
    }

    public Conference findById(Long idconf) {
        if (idconf == null) {
            return null;
        }
        return conferenceRepository.findById(idconf).orElse(null);
    }

    private void mergeWithExistingConference(Conference conference) {
        if (conference.getIdconf() == null) {
            return;
        }

        Conference current = findById(conference.getIdconf());
        if (current == null) {
            return;
        }

        if (isBlank(conference.getNom())) {
            conference.setNom(current.getNom());
        }
        if (isBlank(conference.getTitleconf())) {
            conference.setTitleconf(current.getTitleconf());
        }
        if (conference.getNbeditionconf() == null) {
            conference.setNbeditionconf(current.getNbeditionconf());
        }
        if (isBlank(conference.getDtstartconf())) {
            conference.setDtstartconf(current.getDtstartconf());
        }
        if (isBlank(conference.getDtendconf())) {
            conference.setDtendconf(current.getDtendconf());
        }
        if (isBlank(conference.getUrlwebsiteconf())) {
            conference.setUrlwebsiteconf(current.getUrlwebsiteconf());
        }
        if (conference.getOrganizer() == null) {
            conference.setOrganizer(current.getOrganizer());
        }
        if (conference.getParticipants() == null || conference.getParticipants().isEmpty()) {
            conference.setParticipants(current.getParticipants());
        }
        if (conference.getThematiques() == null || conference.getThematiques().isEmpty()) {
            conference.setThematiques(current.getThematiques());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

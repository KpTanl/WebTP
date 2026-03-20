package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miage.spring.demo.model.Conference;

@Service
public class ConferenceService {
    
    
    private ConferenceRepository conferenceRepository;

    @Autowired
    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public List<Conference> findAll() {
        return conferenceRepository.findAll();
    }

    public Conference addConference(Conference conference) {
        return conferenceRepository.save(conference);
    }

    public Conference updateConference(Conference conference) {
        return conferenceRepository.save(conference);
    }

    public void deleteById(Long idconf) {
        conferenceRepository.deleteById(idconf);
    }

    public Conference findById(Long idconf) {
        return conferenceRepository.findById(idconf).get();
    }
}

package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.stereotype.Service;

import miage.spring.demo.model.Status;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    public Status findById(Long codeStatut) {
        if (codeStatut == null) {
            return null;
        }
        return statusRepository.findById(codeStatut).orElse(null);
    }

    public Status findByNomStatut(String nomStatut) {
        return statusRepository.findByNomStatutIgnoreCase(nomStatut);
    }

    public Status save(Status status) {
        return statusRepository.save(status);
    }
}

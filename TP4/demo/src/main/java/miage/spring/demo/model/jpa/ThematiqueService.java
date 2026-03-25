package miage.spring.demo.model.jpa;

import java.util.List;

import org.springframework.stereotype.Service;

import miage.spring.demo.model.Thematique;

@Service
public class ThematiqueService {

    private final ThematiqueRepository thematiqueRepository;

    public ThematiqueService(ThematiqueRepository thematiqueRepository) {
        this.thematiqueRepository = thematiqueRepository;
    }

    public List<Thematique> findAll() {
        return thematiqueRepository.findAll();
    }

    public Thematique findById(Long idThematique) {
        if (idThematique == null) {
            return null;
        }
        return thematiqueRepository.findById(idThematique).orElse(null);
    }

    public Thematique findByNomThematique(String nomThematique) {
        return thematiqueRepository.findByNomThematiqueIgnoreCase(nomThematique);
    }

    public boolean existsByNomThematique(String nomThematique) {
        return thematiqueRepository.existsByNomThematiqueIgnoreCase(nomThematique);
    }

    public Thematique save(Thematique thematique) {
        return thematiqueRepository.save(thematique);
    }
}

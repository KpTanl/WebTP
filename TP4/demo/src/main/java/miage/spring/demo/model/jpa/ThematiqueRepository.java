package miage.spring.demo.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import miage.spring.demo.model.Thematique;

@Repository
public interface ThematiqueRepository extends JpaRepository<Thematique, Long> {
    Thematique findByNomThematiqueIgnoreCase(String nomThematique);

    boolean existsByNomThematiqueIgnoreCase(String nomThematique);
}

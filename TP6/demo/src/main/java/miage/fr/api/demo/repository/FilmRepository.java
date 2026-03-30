package miage.fr.api.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import miage.fr.api.demo.Entity.FilmEntity;

public interface FilmRepository extends JpaRepository<FilmEntity, Long> {
    Optional<FilmEntity> findById(Long id);
}

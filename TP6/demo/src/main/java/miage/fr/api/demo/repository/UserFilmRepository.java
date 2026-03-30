package miage.fr.api.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import miage.fr.api.demo.Entity.FilmEntity;
import miage.fr.api.demo.Entity.User;
import miage.fr.api.demo.Entity.UserFilm;

public interface UserFilmRepository extends JpaRepository<UserFilm, Long> {
    Optional<UserFilm> findByUserAndFilm(User user, FilmEntity film);
    List<UserFilm> findByUserAndFavoriteTrue(User user);
    List<UserFilm> findByUserAndWatchlistTrue(User user);
}

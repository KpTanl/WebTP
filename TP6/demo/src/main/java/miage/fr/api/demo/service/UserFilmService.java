package miage.fr.api.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import miage.fr.api.demo.Entity.FilmEntity;
import miage.fr.api.demo.Entity.User;
import miage.fr.api.demo.Entity.UserFilm;
import miage.fr.api.demo.dto.FilmDTO;
import miage.fr.api.demo.repository.FilmRepository;
import miage.fr.api.demo.repository.UserFilmRepository;
import miage.fr.api.demo.repository.UserRepository;

@Service
public class UserFilmService {
    @Autowired
    private UserFilmRepository userFilmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmService filmService;

    public void addToFavorite(Long userId, Long filmId) {
        UserFilm userFilm = getOrCreateUserFilm(userId, filmId);
        userFilm.setFavorite(true);
        userFilmRepository.save(userFilm);
    }

    public void removeFromFavorite(Long userId, Long filmId) {
        UserFilm userFilm = getOrCreateUserFilm(userId, filmId);
        userFilm.setFavorite(false);
        userFilmRepository.save(userFilm);
    }

    public void addToWatchlist(Long userId, Long filmId) {
        UserFilm userFilm = getOrCreateUserFilm(userId, filmId);
        userFilm.setWatchlist(true);
        userFilmRepository.save(userFilm);
    }

    public void removeFromWatchlist(Long userId, Long filmId) {
        UserFilm userFilm = getOrCreateUserFilm(userId, filmId);
        userFilm.setWatchlist(false);
        userFilmRepository.save(userFilm);
    }

    public List<FilmEntity> getFavoriteFilms(Long userId) {
        List<UserFilm> userFilms = userFilmRepository.findByUserAndFavoriteTrue(getUser(userId));
        List<FilmEntity> films = new ArrayList<>();
        for (UserFilm userFilm : userFilms) {
            films.add(userFilm.getFilm());
        }
        return films;
    }

    public List<FilmEntity> getWatchlistFilms(Long userId) {
        List<UserFilm> userFilms = userFilmRepository.findByUserAndWatchlistTrue(getUser(userId));
        List<FilmEntity> films = new ArrayList<>();
        for (UserFilm userFilm : userFilms) {
            films.add(userFilm.getFilm());
        }
        return films;
    }

    public boolean isFavorite(Long userId, Long filmId) {
        Optional<UserFilm> optionalUserFilm = findUserFilm(userId, filmId);
        if (optionalUserFilm.isEmpty()) {
            return false;
        }
        return Boolean.TRUE.equals(optionalUserFilm.get().getFavorite());
    }

    public boolean isInWatchlist(Long userId, Long filmId) {
        Optional<UserFilm> optionalUserFilm = findUserFilm(userId, filmId);
        if (optionalUserFilm.isEmpty()) {
            return false;
        }
        return Boolean.TRUE.equals(optionalUserFilm.get().getWatchlist());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    private UserFilm getOrCreateUserFilm(Long userId, Long filmId) {
        User user = getUser(userId);
        FilmEntity film = getOrCreateFilm(filmId);
        Optional<UserFilm> optionalUserFilm = userFilmRepository.findByUserAndFilm(user, film);
        if (optionalUserFilm.isPresent()) {
            return optionalUserFilm.get();
        }

        UserFilm userFilm = new UserFilm();
        userFilm.setUser(user);
        userFilm.setFilm(film);
        userFilm.setFavorite(false);
        userFilm.setWatchlist(false);
        return userFilm;
    }


    // private method to find a userfilm by user and film
    private Optional<UserFilm> findUserFilm(Long userId, Long filmId) {
        User user = getUser(userId);
        Optional<FilmEntity> optionalFilm = filmRepository.findById(filmId);
        if (optionalFilm.isEmpty()) {
            return Optional.empty();
        }
        return userFilmRepository.findByUserAndFilm(user, optionalFilm.get());
    }

    private FilmEntity getOrCreateFilm(Long filmId) {
        Optional<FilmEntity> optionalFilm = filmRepository.findById(filmId);
        if (optionalFilm.isPresent()) {
            return optionalFilm.get();
        }

        FilmDTO filmDTO = filmService.getFilmById(filmId);
        FilmEntity film = new FilmEntity();
        film.setId(filmDTO.getId());
        film.setTitle(filmDTO.getTitle());
        film.setReleaseDate(filmDTO.getReleaseDate());
        film.setVoteAverage(filmDTO.getVoteAverage());
        film.setPosterPath(filmDTO.getPosterPath());
        film.setOverview(filmDTO.getOverview());
        return filmRepository.save(film);
    }
}

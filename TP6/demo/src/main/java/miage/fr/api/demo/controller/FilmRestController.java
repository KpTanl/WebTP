package miage.fr.api.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import miage.fr.api.demo.dto.FilmDTO;
import miage.fr.api.demo.dto.FilmPageDTO;
import miage.fr.api.demo.service.FilmService;

@RestController
@RequestMapping("/api")
public class FilmRestController {
    
    private final FilmService filmService;

    public FilmRestController(FilmService filmService) {
        this.filmService = filmService;
    }
    

    @GetMapping("/page/{page}")
    public FilmPageDTO getPopularFilms(@PathVariable int page) {
        return filmService.getPopularFilms(page);
    }

    @GetMapping("/id/{id}")
    public FilmDTO getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }
}
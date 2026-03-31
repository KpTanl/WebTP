package miage.fr.api.demo.controller;

import java.security.Principal;

import miage.fr.api.demo.Entity.User;
import miage.fr.api.demo.dto.FilmDTO;
import miage.fr.api.demo.dto.FilmPageDTO;
import miage.fr.api.demo.service.FilmService;
import miage.fr.api.demo.service.UserFilmService;
import miage.fr.api.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;
    private final UserFilmService userFilmService;

    public FilmController(
            FilmService filmService,
            UserService userService,
            UserFilmService userFilmService) {
        this.filmService = filmService;
        this.userService = userService;
        this.userFilmService = userFilmService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {
        try {
            userService.register(name, password);
            redirectAttributes.addAttribute("registered", "");
            return "redirect:/login";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addAttribute("error", exception.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/films";
    }

    @GetMapping("/films")
    public String getPopularFilms(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            Model model) {
        if (page == null || page < 1) {
            page = 1;
        }
        FilmPageDTO filmPageDTO = filmService.getPopularFilms(page);
        model.addAttribute("filmPage", filmPageDTO);
        model.addAttribute("films", filmPageDTO.getResults());
        return "films";
    }

    @GetMapping("/films/{id}")
    public String getFilmById(@PathVariable Long id, Principal principal, Model model) {
        FilmDTO filmDTO = filmService.getFilmById(id);
        User user = userService.getByName(principal.getName());
        model.addAttribute("film", filmDTO);
        model.addAttribute("isFavorite", userFilmService.isFavorite(user.getId(), id));
        model.addAttribute("isInWatchlist", userFilmService.isInWatchlist(user.getId(), id));
        return "film-detail";
    }

    @GetMapping("/favorites")
    public String getFavorites(Principal principal, Model model) {
        User user = userService.getByName(principal.getName());
        model.addAttribute("pageTitle", "Mes films favoris");
        model.addAttribute("films", userFilmService.getFavoriteFilms(user.getId()));
        model.addAttribute("emptyMessage", "Vous n'avez pas encore de films favoris.");
        return "user-films";
    }

    @GetMapping("/watchlist")
    public String getWatchlist(Principal principal, Model model) {
        User user = userService.getByName(principal.getName());
        model.addAttribute("pageTitle", "Mes films a voir");
        model.addAttribute("films", userFilmService.getWatchlistFilms(user.getId()));
        model.addAttribute("emptyMessage", "Vous n'avez pas encore de films a voir.");
        return "user-films";
    }

    @PostMapping("/films/{id}/favorite/add")
    public String addToFavorite(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.getByName(principal.getName());
        userFilmService.addToFavorite(user.getId(), id);
        redirectAttributes.addFlashAttribute("message", "Film ajoute aux favoris");
        return "redirect:/films/" + id;
    }

    @PostMapping("/films/{id}/favorite/remove")
    public String removeFromFavorite(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.getByName(principal.getName());
        userFilmService.removeFromFavorite(user.getId(), id);
        redirectAttributes.addFlashAttribute("message", "Film retire des favoris");
        return "redirect:/films/" + id;
    }

    @PostMapping("/films/{id}/watchlist/add")
    public String addToWatchlist(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.getByName(principal.getName());
        userFilmService.addToWatchlist(user.getId(), id);
        redirectAttributes.addFlashAttribute("message", "Film ajoute a la liste a voir");
        return "redirect:/films/" + id;
    }

    @PostMapping("/films/{id}/watchlist/remove")
    public String removeFromWatchlist(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.getByName(principal.getName());
        userFilmService.removeFromWatchlist(user.getId(), id);
        redirectAttributes.addFlashAttribute("message", "Film retire de la liste a voir");
        return "redirect:/films/" + id;
    }


}

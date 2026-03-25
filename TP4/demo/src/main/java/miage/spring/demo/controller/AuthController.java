package miage.spring.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import miage.spring.demo.model.Conference;
import miage.spring.demo.model.User;
import miage.spring.demo.model.jpa.ConferenceService;
import miage.spring.demo.model.jpa.UserService;

@Controller
public class AuthController {

    public static final String SESSION_USER_ID = "connectedUserId";

    private final UserService userService;
    private final ConferenceService conferenceService;

    public AuthController(UserService userService, ConferenceService conferenceService) {
        this.userService = userService;
        this.conferenceService = conferenceService;
    }

    @GetMapping("/users/login")
    public String loginForm(Model model) {
        model.addAttribute("message", "Connectez-vous avec votre email et votre mot de passe.");
        return "login";
    }


    
    @PostMapping("/users/login")
    public String login(@RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        if (isBlank(email) || isBlank(password)) {
            model.addAttribute("message", "L'email et le mot de passe sont obligatoires.");
            return "login";
        }

        User user = userService.authenticate(email.trim(), password);
        if (user == null) {
            model.addAttribute("message", "Connexion impossible : email ou mot de passe incorrect.");
            return "login";
        }

        session.setAttribute(SESSION_USER_ID, user.getId());
        return showUserSpace(model, user, "Connexion réussie.");
    }

    @GetMapping("/users/me/conferences")
    public String myConferences(HttpSession session, Model model) {
        User user = getConnectedUser(session);
        if (user == null) {
            model.addAttribute("message", "Veuillez vous connecter pour accéder à votre espace.");
            return "login";
        }
        return showUserSpace(model, user, "Bienvenue dans votre espace utilisateur.");
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        model.addAttribute("message", "Déconnexion réussie.");
        return "login";
    }

    private User getConnectedUser(HttpSession session) {
        Object userId = session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            return null;
        }
        Long connectedUserId = (Long) userId;
        return userService.findById(connectedUserId);
    }

    private String showUserSpace(Model model, User user, String message) {
        List<Conference> organizedConferences = conferenceService.findByOrganizerId(user.getId());
        List<Conference> participatedConferences = conferenceService.findByParticipantId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("organizedConferences", organizedConferences);
        model.addAttribute("participatedConferences", participatedConferences);
        model.addAttribute("message", message);
        model.addAttribute("sessionMode", true);
        return "userConferences";
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

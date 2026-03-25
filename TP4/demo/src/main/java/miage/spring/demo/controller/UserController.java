package miage.spring.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import miage.spring.demo.model.Conference;
import miage.spring.demo.model.Status;
import miage.spring.demo.model.User;
import miage.spring.demo.model.jpa.ConferenceService;
import miage.spring.demo.model.jpa.StatusService;
import miage.spring.demo.model.jpa.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final ConferenceService conferenceService;
    private final StatusService statusService;

    public UserController(UserService userService, ConferenceService conferenceService, StatusService statusService) {
        this.userService = userService;
        this.conferenceService = conferenceService;
        this.statusService = statusService;
    }

    @GetMapping("/users/add/form")
    public String addUserForm(Model model) {
        model.addAttribute("statuses", statusService.findAll());
        return "addUser";
    }

    @GetMapping("/users/update/form")
    public String updateUserForm(Model model) {
        model.addAttribute("statuses", statusService.findAll());
        return "updateUser";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("user") User user,
            @RequestParam("statusId") Long statusId,
            Model model) {
        Status status = statusService.findById(statusId);
        if (status == null) {
            return showUserResult(model, "Ajout impossible : statut introuvable.", userService.findAll());
        }
        if (userService.emailExists(user.getEmail())) {
            return showUserResult(model, "Ajout impossible : cet email existe deja.", userService.findAll());
        }
        if (isBlank(user.getPassword())) {
            return showUserResult(model, "Ajout impossible : le mot de passe est obligatoire.", userService.findAll());
        }

        user.setStatus(status);
        userService.addUser(user);
        return showUserResult(model, "Utilisateur ajoute avec succes.", userService.findAll());
    }

    @GetMapping("/users/all")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateurs trouves avec succes.");
        return "allUser";
    }

    @PostMapping("/users/search")
    public String searchUser(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return showUserResult(model, "Utilisateur non trouve.", new ArrayList<User>());
        }

        List<User> users = new ArrayList<>();
        users.add(user);
        return showUserResult(model, "Resultat de la recherche.", users);
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) {
        if (userService.findById(id) == null) {
            return showUserResult(model, "Suppression impossible : utilisateur introuvable.", userService.findAll());
        }
        userService.deleteById(id);
        return showUserResult(model, "Utilisateur supprime avec succes.", userService.findAll());
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") User user,
            @RequestParam("statusId") Long statusId,
            Model model) {
        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            return showUserResult(model, "Modification impossible : utilisateur introuvable.", userService.findAll());
        }

        Status status = statusService.findById(statusId);
        if (status == null) {
            return showUserResult(model, "Modification impossible : statut introuvable.", userService.findAll());
        }
        if (userService.emailExistsForAnotherUser(user.getId(), user.getEmail())) {
            return showUserResult(model, "Modification impossible : cet email appartient deja a un autre utilisateur.", userService.findAll());
        }

        user.setStatus(status);
        userService.updateUser(user);
        return showUserResult(model, "Utilisateur modifie avec succes.", userService.findAll());
    }

    @PostMapping("/users/all")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateurs trouves avec succes.");
        return "allUser";
    }

    @PostMapping("/users/conferences")
    public String searchUserConferences(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("organizedConferences", conferenceService.findByOrganizerId(user.getId()));
            model.addAttribute("participatedConferences", conferenceService.findByParticipantId(user.getId()));
            model.addAttribute("message", "Conférences de l'utilisateur trouvé.");
            model.addAttribute("sessionMode", false);
            return "userConferences";
        }
        return showUserResult(model, "Utilisateur non trouve.", new ArrayList<User>());
    }

    @PostMapping("/users/participate")
    public String addParticipation(@RequestParam("userId") Long userId,
            @RequestParam("conferenceId") Long conferenceId,
            Model model) {
        try {
            User user = userService.findById(userId);
            Conference conference = conferenceService.findById(conferenceId);

            if (user != null && conference != null) {
                user.addParticipation(conference);
                userService.updateUser(user);
                return showUserResult(model, "Utilisateur inscrit a la conference avec succes.", userService.findAll());
            }
            return showUserResult(model, "Utilisateur ou conference introuvable.", userService.findAll());
        } catch (Exception e) {
            return showUserResult(model, "Erreur : ID introuvable.", userService.findAll());
        }
    }

    @PostMapping("/users/unparticipate")
    public String removeParticipation(@RequestParam("userId") Long userId,
            @RequestParam("conferenceId") Long conferenceId,
            Model model) {
        try {
            User user = userService.findById(userId);
            Conference conference = conferenceService.findById(conferenceId);

            if (user != null && conference != null) {
                user.removeParticipation(conference);
                userService.updateUser(user);
                return showUserResult(model, "Utilisateur desinscrit de la conference avec succes.", userService.findAll());
            }
            return showUserResult(model, "Utilisateur ou conference introuvable.", userService.findAll());
        } catch (Exception e) {
            return showUserResult(model, "Erreur : ID introuvable.", userService.findAll());
        }
    }

    private String showUserResult(Model model, String message, List<User> users) {
        model.addAttribute("users", users);
        model.addAttribute("message", message);
        return "resultUser";
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

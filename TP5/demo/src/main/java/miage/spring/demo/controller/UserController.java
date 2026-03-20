package miage.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import miage.spring.demo.model.User;
import miage.spring.demo.model.Conference;
import miage.spring.demo.model.jpa.UserService;
import miage.spring.demo.model.jpa.ConferenceService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    
    private UserService userService;
    private ConferenceService conferenceService;
    
    @Autowired
    public UserController(UserService userService, ConferenceService conferenceService) {
        this.userService = userService;
        this.conferenceService = conferenceService;
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        userService.addUser(user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateur ajouté avec succès");
        return "resultUser";
    }

    @GetMapping("/users/all")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateurs trouvés avec succès");
        return "allUser";
    }

    @PostMapping("/users/search")
    public String searchUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("users", userService.findById(id));
        model.addAttribute("message", "Résultat de la recherche:");
        return "resultUser";
    }
    
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) {
        userService.deleteById(id);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateur supprimé avec succès");
        return "resultUser";
    }
    
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        userService.updateUser(user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateur modifié avec succès");
        return "resultUser";
    }

    @PostMapping("/users/all")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("message", "Utilisateurs tous trouvés avec succès");
        return "allUser";
    }

    @PostMapping("/users/conferences")
    public String searchUserConferences(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("conferences", user.getConferencesParticipated());
            return "userConferences";
        }
        model.addAttribute("message", "Utilisateur non trouvé");
        return "resultUser";
    }
    
    @PostMapping("/users/participate")
    public String addParticipation(@RequestParam("userId") Long userId, @RequestParam("conferenceId") Long conferenceId, Model model) {
        try {
            User user = userService.findById(userId);
            Conference conference = conferenceService.findById(conferenceId);
            
            if (user != null && conference != null) {
                user.getConferencesParticipated().add(conference);
                userService.updateUser(user);
                model.addAttribute("message", "Utilisateur inscrit à la conférence avec succès");
            } else {
                model.addAttribute("message", "Utilisateur ou conférence introuvable");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Erreur : ID introuvable");
        }
        
        model.addAttribute("users", userService.findAll());
        return "resultUser";
    }

    @PostMapping("/users/unparticipate")
    public String removeParticipation(@RequestParam("userId") Long userId, @RequestParam("conferenceId") Long conferenceId, Model model) {
        try {
            User user = userService.findById(userId);
            Conference conference = conferenceService.findById(conferenceId);
            
            if (user != null && conference != null) {
                user.getConferencesParticipated().remove(conference);
                userService.updateUser(user);
                model.addAttribute("message", "Utilisateur désinscrit de la conférence avec succès");
            } else {
                model.addAttribute("message", "Utilisateur ou conférence introuvable");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Erreur : ID introuvable");
        }
        
        model.addAttribute("users", userService.findAll());
        return "resultUser";
    }
    
}

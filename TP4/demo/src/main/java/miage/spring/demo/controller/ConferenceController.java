package miage.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import miage.spring.demo.model.Conference;
import miage.spring.demo.model.User;
import miage.spring.demo.model.jpa.ConferenceService;
import miage.spring.demo.model.jpa.UserService;

@Controller
public class ConferenceController {

    private ConferenceService conferenceService;
    private UserService userService;

    @Autowired
    public ConferenceController(ConferenceService conferenceService, UserService userService) {
        this.conferenceService = conferenceService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String MenuConferences(Model model) {
        return "redirect:/menu.html";
    }

    @PostMapping("/conferences/add")
    public String addConference(@ModelAttribute("conference") Conference conference,
            @RequestParam("organizerId") Long organizerId, Model model) {
        User organizer = userService.findById(organizerId);
        if (organizer == null) {
            model.addAttribute("conferences", conferenceService.findAll());
            model.addAttribute("message", "Ajout impossible : organisateur introuvable");
            return "resultConference";
        }
        conference.setOrganizer(organizer);
        conferenceService.addConference(conference);
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conference ajoutee avec succes");
        return "resultConference";
    }

    @GetMapping("/conferences/all")
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conferences trouvees avec succes");
        return "allConference";
    }

    @PostMapping("/conferences/search")
    public String searchConference(@RequestParam("titleconf") String titleconf, Model model) {
        int matches = conferenceService.findByTitleconf(titleconf).size();
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Resultat de la recherche pour \"" + titleconf + "\" : " + matches
                + " conference(s) trouvee(s)");
        return "resultConference";
    }

    @PostMapping("/conferences/delete")
    public String deleteConference(@RequestParam("idconf") Long idconf, Model model) {
        conferenceService.deleteById(idconf);
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conference supprimee avec succes");
        return "resultConference";
    }

    @PostMapping("/conferences/update")
    public String updateConference(@ModelAttribute("conference") Conference conference,
            @RequestParam("organizerId") Long organizerId, Model model) {
        User organizer = userService.findById(organizerId);
        if (organizer == null) {
            model.addAttribute("conferences", conferenceService.findAll());
            model.addAttribute("message", "Modification impossible : organisateur introuvable");
            return "resultConference";
        }
        conference.setOrganizer(organizer);
        conferenceService.updateConference(conference);
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conference modifiee avec succes");
        return "resultConference";
    }
}

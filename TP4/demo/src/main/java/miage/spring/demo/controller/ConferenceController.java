package miage.spring.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import miage.spring.demo.model.Conference;
import miage.spring.demo.model.Thematique;
import miage.spring.demo.model.User;
import miage.spring.demo.model.jpa.ConferenceService;
import miage.spring.demo.model.jpa.ThematiqueService;
import miage.spring.demo.model.jpa.UserService;

@Controller
public class ConferenceController {

    private final ConferenceService conferenceService;
    private final UserService userService;
    private final ThematiqueService thematiqueService;

    public ConferenceController(ConferenceService conferenceService, UserService userService,
            ThematiqueService thematiqueService) {
        this.conferenceService = conferenceService;
        this.userService = userService;
        this.thematiqueService = thematiqueService;
    }

    @GetMapping("/")
    public String menuConferences() {
        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/conferences/add/form")
    public String addConferenceForm(Model model) {
        populateConferenceForm(model);
        return "addConference";
    }

    @GetMapping("/conferences/update/form")
    public String updateConferenceForm(Model model) {
        populateConferenceForm(model);
        return "updateConference";
    }

    @PostMapping("/conferences/add")
    public String addConference(@ModelAttribute("conference") Conference conference,
            @RequestParam("organizerId") Long organizerId,
            @RequestParam("thematiqueIds") List<String> thematiqueIds,
            Model model) {
        User organizer = userService.findById(organizerId);
        if (organizer == null) {
            return showConferenceResult(model, "Ajout impossible : organisateur introuvable.", conferenceService.findAll());
        }

        try {
            conference.setOrganizer(organizer);
            conference.setThematiques(parseThematiques(thematiqueIds));
            conferenceService.addConference(conference);
            return showConferenceResult(model, "Conference ajoutee avec succes.", conferenceService.findAll());
        } catch (IllegalArgumentException e) {
            return showConferenceResult(model, e.getMessage(), conferenceService.findAll());
        }
    }

    @GetMapping("/conferences/all")
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conferences trouvees avec succes.");
        return "allConference";
    }

    @PostMapping("/conferences/search")
    public String searchConference(@RequestParam("titleconf") String titleconf, Model model) {
        List<Conference> conferences = conferenceService.findByTitleContaining(titleconf);
        String message = conferences.isEmpty()
                ? "Aucune conference trouvee pour : " + titleconf
                : "Resultat de la recherche pour : " + titleconf;
        return showConferenceResult(model, message, conferences);
    }

    @PostMapping("/conferences/delete")
    public String deleteConference(@RequestParam("idconf") Long idconf, Model model) {
        if (conferenceService.findById(idconf) == null) {
            return showConferenceResult(model, "Suppression impossible : conference introuvable.", conferenceService.findAll());
        }
        conferenceService.deleteById(idconf);
        return showConferenceResult(model, "Conference supprimee avec succes.", conferenceService.findAll());
    }

    @PostMapping("/conferences/update")
    public String updateConference(@ModelAttribute("conference") Conference conference,
            @RequestParam("organizerId") Long organizerId,
            @RequestParam("thematiqueIds") List<String> thematiqueIds,
            Model model) {
        if (conferenceService.findById(conference.getIdconf()) == null) {
            return showConferenceResult(model, "Modification impossible : conference introuvable.", conferenceService.findAll());
        }

        User organizer = userService.findById(organizerId);
        if (organizer == null) {
            return showConferenceResult(model, "Modification impossible : organisateur introuvable.", conferenceService.findAll());
        }

        try {
            conference.setOrganizer(organizer);
            conference.setThematiques(parseThematiques(thematiqueIds));
            conferenceService.updateConference(conference);
            return showConferenceResult(model, "Conference modifiee avec succes.", conferenceService.findAll());
        } catch (IllegalArgumentException e) {
            return showConferenceResult(model, e.getMessage(), conferenceService.findAll());
        }
    }

    private String showConferenceResult(Model model, String message, List<Conference> conferences) {
        model.addAttribute("conferences", conferences);
        model.addAttribute("message", message);
        return "resultConference";
    }

    private void populateConferenceForm(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("thematiques", thematiqueService.findAll());
    }

    private Set<Thematique> parseThematiques(List<String> thematiqueIds) {
        if (thematiqueIds == null || thematiqueIds.isEmpty()) {
            throw new IllegalArgumentException("Veuillez renseigner au moins un ID de thematique.");
        }

        Set<Thematique> thematiques = new HashSet<>();

        try {
            for (String value : thematiqueIds) {
                if (value == null || value.isBlank()) {
                    continue;
                }

                String[] parts = value.split(",");
                for (String part : parts) {
                    String cleanedValue = part.trim();
                    if (cleanedValue.isBlank()) {
                        continue;
                    }

                    Long idThematique = Long.valueOf(cleanedValue);
                    Thematique thematique = thematiqueService.findById(idThematique);

                    if (thematique == null) {
                        throw new IllegalArgumentException("Une ou plusieurs thematiques sont introuvables.");
                    }

                    thematiques.add(thematique);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Les IDs de thematiques doivent etre des nombres separes par des virgules.");
        }

        if (thematiques.isEmpty()) {
            throw new IllegalArgumentException("Une ou plusieurs thematiques sont introuvables.");
        }

        return thematiques;
    }
}

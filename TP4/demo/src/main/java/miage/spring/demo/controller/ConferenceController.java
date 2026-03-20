package miage.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import miage.spring.demo.model.Conference;
import miage.spring.demo.model.jpa.ConferenceService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ConferenceController {
    
    private ConferenceService conferenceService;
    
    @Autowired
    public ConferenceController(ConferenceService conferenceService) {
    this.conferenceService = conferenceService;
}

    @GetMapping("/")
    public String MenuConferences(Model model) {
        return "redirect:/menu.html";
    }

    @PostMapping("/conferences/add")
    public String addConference(@ModelAttribute("conference") Conference conference, Model model) {
        conferenceService.addConference(conference);
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conférence ajoutée avec succès");
        return "resultConference";
    }

    @GetMapping("/conferences/all")
    public String listConferences(Model model) {
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conférences trouvées avec succès");
        return "allConference";
    }

    @PostMapping("/conferences/search")
    public String searchConference(@RequestParam("idconf") Long idconf, Model model) {
        model.addAttribute("conferences", conferenceService.findById(idconf));
        model.addAttribute("message", "Résultat de la recherche:");
        return "resultConference";
    }
    
    @PostMapping("/conferences/delete")
    public String deleteConference(@RequestParam("idconf") Long idconf, Model model) {
        conferenceService.deleteById(idconf);
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conférence supprimée avec succès");
        return "resultConference";
    }
    
    @PostMapping("/conferences/update")
    public String updateConference(@ModelAttribute("conference") Conference conference, Model model) {
        conferenceService.updateConference(conference);
        model.addAttribute("conferences", conferenceService.findAll());
        model.addAttribute("message", "Conférence modifiée avec succès");
        return "resultConference";
    }
    
    
}

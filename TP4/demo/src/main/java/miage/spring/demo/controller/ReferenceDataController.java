package miage.spring.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import miage.spring.demo.model.Status;
import miage.spring.demo.model.Thematique;
import miage.spring.demo.model.jpa.StatusService;
import miage.spring.demo.model.jpa.ThematiqueService;

@Controller
public class ReferenceDataController {

    private final StatusService statusService;
    private final ThematiqueService thematiqueService;

    public ReferenceDataController(StatusService statusService, ThematiqueService thematiqueService) {
        this.statusService = statusService;
        this.thematiqueService = thematiqueService;
    }

    @PostMapping("/statuses/add")
    public String addStatus(@RequestParam("nomStatut") String nomStatut, Model model) {
        if (nomStatut == null || nomStatut.isBlank()) {
            model.addAttribute("message", "Le nom du statut est obligatoire.");
        } else if (statusService.findByNomStatut(nomStatut) != null) {
            model.addAttribute("message", "Ce statut existe deja.");
        } else {
            statusService.save(new Status(nomStatut.trim()));
            model.addAttribute("message", "Statut ajoute avec succes.");
        }
        model.addAttribute("statuses", statusService.findAll());
        return "allStatus";
    }

    @GetMapping("/statuses/all")
    public String listStatuses(Model model) {
        model.addAttribute("statuses", statusService.findAll());
        model.addAttribute("message", "Liste des statuts.");
        return "allStatus";
    }

    @PostMapping("/thematiques/add")
    public String addThematique(@RequestParam("nomThematique") String nomThematique, Model model) {
        if (nomThematique == null || nomThematique.isBlank()) {
            model.addAttribute("message", "Le nom de la thematique est obligatoire.");
        } else if (thematiqueService.existsByNomThematique(nomThematique)) {
            model.addAttribute("message", "Cette thematique existe deja.");
        } else {
            thematiqueService.save(new Thematique(nomThematique.trim()));
            model.addAttribute("message", "Thematique ajoutee avec succes.");
        }
        model.addAttribute("thematiques", thematiqueService.findAll());
        return "allThematique";
    }

    @GetMapping("/thematiques/all")
    public String listThematiques(Model model) {
        model.addAttribute("thematiques", thematiqueService.findAll());
        model.addAttribute("message", "Liste des thematiques.");
        return "allThematique";
    }
}

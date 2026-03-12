package utcapitole.miage.tp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import utcapitole.miage.tp2.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ResaController {
    @RequestMapping("/resa/")
    public String index() {
        return "redirect:/resa/index.html";
    }

    private static List<Reservation> reservations = new ArrayList<>();

    @PostMapping("/resa/save")
    public String saveReservation(
            @RequestParam("nb_personnes") int nbPersonnes,
            @RequestParam("date_reservation") String dateReservation,
            @RequestParam("destination") String destination,
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam(value = "paiement", required = false) String paiement,
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "offres", required = false) String offres,
            @RequestParam(value = "conditions", required = false) String conditions) {

        boolean isOffres = offres != null && offres.equals("oui");
        boolean isConditions = conditions != null && conditions.equals("oui");

        Reservation reservation = new Reservation(nbPersonnes, dateReservation, destination, nom, email, paiement, iban,
                isOffres, isConditions);
        reservations.add(reservation);

        return "redirect:/resa/confirmation.html";
    }

    @GetMapping("/resa/all")
    @ResponseBody
    public List<Reservation> getAllReservations() {
        return reservations;
    }
    

}

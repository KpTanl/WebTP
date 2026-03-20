package utcapitole.miage.tp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ui.Model;

import utcapitole.miage.tp2.model.Reservation;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ResaController {
    @RequestMapping("/resa/")
    public String index(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        model.addAttribute("user", user);
        return "index";
    }

    @PostMapping("/resa/login")
    public String login(@RequestParam("username") String username, HttpSession session) {
        session.setAttribute("user", username);
        return "redirect:/resa/";
    }

    @GetMapping("/resa/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        reservations.clear();
        return "redirect:/resa/";
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

        return "redirect:/resa/all";
    }

    @GetMapping("/resa/all")
    public String getAllReservations(Model model, HttpSession session) {
        String user = (String) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("reservations", reservations);
        return "reservation";
    }
    
    @GetMapping("/resa/delete/{index}")
    public String deleteReservation(@PathVariable ("index") int index) {
        if (index >= 0 && index < reservations.size()) {
            reservations.remove(index);
        }
        return "redirect:/resa/all";
    }
}

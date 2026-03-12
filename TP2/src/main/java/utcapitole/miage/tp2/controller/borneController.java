package utcapitole.miage.tp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
public class borneController {
    @GetMapping("/bornes")
    public String bornes(@RequestParam("borne_inf") int borne_inf, @RequestParam("borne_sup") int borne_sup) {
        StringBuilder html = new StringBuilder();
        
        html.append("<table border=1>");
        html.append("<tr>");
        html.append("<th> Nombre </th>");
        html.append("<th> Carre </th>");
        html.append("</tr>");

        for (int i = borne_inf; i <= borne_sup; i++) {
            html.append("<tr>");
                html.append("<td>" + i + "</td>");
                html.append("<td>" + i * i + "</td>");
            html.append("</tr>");
        }

        html.append("</table>");    

        return html.toString();
    }
}

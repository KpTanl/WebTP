package utcapitole.miage.tp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/hello")
public class Formulaire {
    
    @GetMapping("/world_2/{nom}")
    public String world_2(@PathVariable String nom) {
        return "hello "+nom;
    }


    @GetMapping("/formu")
    public String formulaire() {
        return "forward:/formu.html";
    }
}

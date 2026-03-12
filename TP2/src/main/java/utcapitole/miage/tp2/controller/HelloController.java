package utcapitole.miage.tp2.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/world")
    public String hello(@RequestParam(value = "name", defaultValue = "world") String name, Model model) {
        return "hello"+name;
    }

    @GetMapping("/person/{name}")
    public String helloPerson(@PathVariable String name) {
        return "hello "+name;
    }

}



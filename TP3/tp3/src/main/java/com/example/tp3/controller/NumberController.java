package com.example.tp3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tp3.model.Number;

@Controller
public class NumberController {

    @GetMapping("/")
    public String showNumber() {
        return "index";
    }

    @PostMapping("/calculate")
    public String calculateNumber(@RequestParam ("numbre_low") int numbreLow,
        @RequestParam ("numbre_high") int numbreHigh, Model model) {
        List<Number> list = new ArrayList<>();
        for (int i = numbreLow; i <= numbreHigh; i++) {
            list.add(new Number(i,i*i));
        }
       model.addAttribute("lower", numbreLow);
       model.addAttribute("upper", numbreHigh);
       model.addAttribute("results", list);

        return "res";
    }
  
}

package com.example.demo.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiaryController {

    @GetMapping("/diary")
    public String diary(@RequestParam(required = false) String date, Model model) {
        model.addAttribute("date", date); // 없으면 null
        return "diary"; // diary.html
    }
}


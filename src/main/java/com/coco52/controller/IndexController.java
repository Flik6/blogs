package com.coco52.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping(value = {"/index","/index.html","/"})
    public String index() {
        return "index";
    }

    @GetMapping("/health")
    public String health() {
        return "campusToday/health";
    }
    @GetMapping("/schoolHelp")
    public String help(){
        return "campusToday/help";
    }
    @GetMapping("/sign")
    public String sign(){
        return "util/signin";
    }

}

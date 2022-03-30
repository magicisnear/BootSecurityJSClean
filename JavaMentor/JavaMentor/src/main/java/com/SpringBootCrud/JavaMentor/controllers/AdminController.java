package com.SpringBootCrud.JavaMentor.controllers;

import com.SpringBootCrud.JavaMentor.service.RoleService;
import com.SpringBootCrud.JavaMentor.service.UserService;
import com.SpringBootCrud.JavaMentor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Controller
public class AdminController {


    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String newPage(@AuthenticationPrincipal User user1, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("user1", user1); // текущий авторизированный пользователь
        return "ExperimentalNewPage";
    }

    @GetMapping("/login1FA")
    public String preloginPage(Model model) {
        return "login1FA";
    }

    @GetMapping("/login1FAQR")
    public String postPage(@RequestParam String name, Model model) throws UnsupportedEncodingException {
        User user = userService.findByName(name).get();
        if (user.isUsing2FA()) {
            model.addAttribute("qrcode", userService.generateQRUrl(user));
            return "qrcode";
        }
        return  "redirect:/login";
    }




}

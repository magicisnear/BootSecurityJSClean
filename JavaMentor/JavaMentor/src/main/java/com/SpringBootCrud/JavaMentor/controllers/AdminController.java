package com.SpringBootCrud.JavaMentor.controllers;

import com.SpringBootCrud.JavaMentor.service.RoleService;
import com.SpringBootCrud.JavaMentor.service.UserService;
import com.SpringBootCrud.JavaMentor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public String newPage(@AuthenticationPrincipal User user1, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users); // список юзеров
        model.addAttribute("user1", user1); // текущий авторизированный пользователь
        model.addAttribute("user2", new User()); // для формы добавления юзеров
        model.addAttribute("setRoles", roleService.getAll()); // для формы добавления юзеров
        return "ExperimentalNewPage";
    }


}

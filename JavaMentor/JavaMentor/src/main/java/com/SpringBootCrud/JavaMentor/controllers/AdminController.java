package com.SpringBootCrud.JavaMentor.controllers;

import com.SpringBootCrud.JavaMentor.exceptions.ThisNameAlreadyExistsException;
import com.SpringBootCrud.JavaMentor.model.Role;
import com.SpringBootCrud.JavaMentor.repository.UserRepository;
import com.SpringBootCrud.JavaMentor.service.RoleService;
import com.SpringBootCrud.JavaMentor.service.UserService;
import com.SpringBootCrud.JavaMentor.service.UserServiceImpl;
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

    @GetMapping("/1")
    public String newPage(@AuthenticationPrincipal User user1, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users); // список юзеров
        model.addAttribute("user1", user1); // текущий авторизированный пользователь
        model.addAttribute("user2", new User()); // для формы добавления юзеров
        model.addAttribute("setRoles", roleService.getAll()); // для формы добавления юзеров
        return "MainPage";
    }

    @GetMapping("/admin")
    public String findAll(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-list";
    }

    @GetMapping("/admin/create")
    public String createUserForm(User user) {
        return "admin-create";
    }

    @PostMapping("/admin/create")
    public String createUser(User user) throws ThisNameAlreadyExistsException {
        if (userService.getAllUsers()
                .contains((userService.findByName(user.getName())))) {
            throw new ThisNameAlreadyExistsException();
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/1";
    }

    @GetMapping("/admin/update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findByID(id);
        List<Role> setRoles = roleService.getAll();
        model.addAttribute("user", user);
        model.addAttribute("setRoles", setRoles);
        return "admin-update";
    }

    @PostMapping("/admin/update")
    public String updateUser(User user) {
        userService.saveUser(user);
        return "redirect:/1";
    }

    //add method for new Site

    @PostMapping("/admin/save")
    public String save(User user) {
        userService.saveUser(user);
        return "redirect:/1";
    }

    @PostMapping("/admin/delete")
    public String delete(Long id, Model model) {
        userService.deleteById(id);
        return "redirect:/1";
    }

    @GetMapping("/admin/findOne")
    @ResponseBody
    public User findOne(Long id) {
        return userService.findByID(id);

    }
}

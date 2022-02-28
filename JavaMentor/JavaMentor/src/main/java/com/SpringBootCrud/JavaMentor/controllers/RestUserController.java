package com.SpringBootCrud.JavaMentor.controllers;

import com.SpringBootCrud.JavaMentor.exceptions.DataInfoHandler;
import com.SpringBootCrud.JavaMentor.exceptions.ThisNameAlreadyExistsException;
import com.SpringBootCrud.JavaMentor.exceptions.UserIdNotFoundException;
import com.SpringBootCrud.JavaMentor.exceptions.UserWithSuchLoginExist;
import com.SpringBootCrud.JavaMentor.model.User;
import com.SpringBootCrud.JavaMentor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserService userService;

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> apiGetAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> apiGetOneUser(@PathVariable("id") long id) {
            return new ResponseEntity<>(userService.findByID(id).get(), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<DataInfoHandler> apiAddNewUser(@RequestBody User user) {
        if (userService.findByName(user.getName()).isPresent()) {
            return new ResponseEntity<>(new DataInfoHandler("Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<DataInfoHandler> apiUpdateUser(@PathVariable("id") long id,
                                                         @RequestBody User user) {
        if (userService.findByName(user.getName()).isPresent()) {
            return new ResponseEntity<>(new DataInfoHandler("Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<DataInfoHandler> apiDeleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(new DataInfoHandler("User was deleted"), HttpStatus.OK);
    }

}

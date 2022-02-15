package com.SpringBootCrud.JavaMentor.service;

import com.SpringBootCrud.JavaMentor.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
 Optional<User> findByID(Long id);
 List<User> getAllUsers();
 User saveUser(User user);
 Optional<User> findByName(String name);
 void deleteById(Long id);
}

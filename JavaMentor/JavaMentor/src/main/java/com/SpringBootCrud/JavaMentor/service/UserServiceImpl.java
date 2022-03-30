package com.SpringBootCrud.JavaMentor.service;

import com.SpringBootCrud.JavaMentor.repository.UserRepository;
import com.SpringBootCrud.JavaMentor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static String APP_NAME = "SpringRegistration";
    public static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format(
                        "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                        APP_NAME, user.getEmail(), user.getSecret(), APP_NAME),
                "UTF-8");
    }

    public Optional<User> findByID(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByUserNameAndFetchRoles(name);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsersAndFetchRoles();
    }


    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


}



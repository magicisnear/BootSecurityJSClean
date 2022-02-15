package com.SpringBootCrud.JavaMentor.controllers;

import com.SpringBootCrud.JavaMentor.exceptions.DataInfoHandler;
import com.SpringBootCrud.JavaMentor.exceptions.ThisNameAlreadyExistsException;
import com.SpringBootCrud.JavaMentor.exceptions.UserIdNotFoundException;
import com.SpringBootCrud.JavaMentor.exceptions.UserWithSuchLoginExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {

    private final DataInfoHandler dataInfoHandler;

    @Autowired
    public UserExceptionController(DataInfoHandler dataInfoHandler) {
        this.dataInfoHandler = dataInfoHandler;
    }

    @ExceptionHandler
    public ResponseEntity<DataInfoHandler> handleException(UserWithSuchLoginExist e) {
        return new ResponseEntity<>(dataInfoHandler.getInstanceWithInfo(e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ThisNameAlreadyExistsException.class)
    public ResponseEntity<Object> exception(ThisNameAlreadyExistsException exception) {
        return new ResponseEntity<>("Пользватель с таким именем уже существует", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserIdNotFoundException.class)
    public ResponseEntity<Object> exception(UserIdNotFoundException exception) {
        return new ResponseEntity<>("Пользватель с таким ID не существует", HttpStatus.NOT_FOUND);
    }
}

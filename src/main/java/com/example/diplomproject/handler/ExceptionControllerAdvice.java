package com.example.diplomproject.handler;

import com.example.diplomproject.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<?> notFoundAds() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<?> notFoundComment() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<?> notFoundImage() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> notFoundUserName() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<?> notAuthorizedUser() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @ExceptionHandler(ForbiddenEntryException.class)
    public ResponseEntity<?> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @ExceptionHandler(IncorrectArgumentException.class)
    public ResponseEntity<?> incorrectArgument() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}

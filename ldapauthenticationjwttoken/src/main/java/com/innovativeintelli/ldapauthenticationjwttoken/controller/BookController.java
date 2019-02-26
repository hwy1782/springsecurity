package com.innovativeintelli.ldapauthenticationjwttoken.controller;

import com.innovativeintelli.ldapauthenticationjwttoken.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {

  @Autowired
  AuthenticationManager authenticationManager;


  @Autowired
  JwtTokenProvider tokenProvider;

  @PostMapping("/list")
  public ResponseEntity<String> authenticateUser() {
    return ResponseEntity.ok("all books");
  }


}
package com.example.controller;

import javax.security.auth.message.AuthException;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public Account registerAccount(@RequestBody Account a) throws Exception {
        return accountService.registerUser(a);
    }

    @PostMapping("/login")
    public Account loginAccount(@RequestBody Account a) throws Exception {
        return accountService.loginUser(a);
    }

    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message m) throws Exception {
        return messageService.createMessage(m);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id){
        Optional<Message> message = messageService.getMessageById(id);
        if (message.isEmpty()) {
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.of(message);
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/accounts/{id}/messages")
    public List<Message> getAllMessagesForUser(@PathVariable int id) {
        return messageService.getAllMessagesForUser(id);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> getAllMessages(@PathVariable int id) {
        int response = messageService.deleteMessageById(id);
        if (response == 0) return ResponseEntity.status(200).build();
        return ResponseEntity.ok(1);
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int id, @RequestBody Message m) {
        int response = messageService.updateMessageById(m, id);
        if (response == 0) return ResponseEntity.status(400).build();
        return ResponseEntity.ok(1);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleBadRequest(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadRequest(AuthException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(Exception ex) {
        return ex.getMessage();
    }


}

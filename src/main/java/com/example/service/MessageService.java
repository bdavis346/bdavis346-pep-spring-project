package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    private boolean validateMessage(Message m) {

        return !m.getMessageText().isBlank() && m.getMessageText().length() < 255;
    }

    public Message createMessage(Message m) throws Exception {
        if (!validateMessage(m)) throw new Exception("Invalid Message Body");
        if (accountRepository.findById(m.getPostedBy()) == null) throw new Exception("Posted By Not Real User.");
        return messageRepository.save(m);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesForUser(Integer accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }

    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public int deleteMessageById(Integer id) {
        try {
            messageRepository.deleteById(id);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    public int updateMessageById(Message m,Integer id) {
        if (!validateMessage(m)) return 0;
        try {
            Message oldMessage = messageRepository.getById(id);
            oldMessage.setMessageText(m.getMessageText());
            messageRepository.save(oldMessage);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

}

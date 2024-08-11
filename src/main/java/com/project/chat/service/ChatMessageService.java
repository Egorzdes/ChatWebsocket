package com.project.chat.service;


import com.project.chat.model.ChatMessage;
import com.project.chat.repository.ChatMessageRepository;
import com.project.chat.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final SecretKey secretKey;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository, SecretKey secretKey) {
        this.chatMessageRepository = chatMessageRepository;
        this.secretKey = secretKey;
    }

    public ChatMessage saveMessage(String sender, String content) throws Exception {
        String encryptedContent = EncryptionUtil.encrypt(content, secretKey);
        ChatMessage message = new ChatMessage(sender,encryptedContent);
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getAllMessages() throws Exception {
        List<ChatMessage> messages = chatMessageRepository.findAll();
        for (ChatMessage message : messages) {
            String decryptedContent = EncryptionUtil.decrypt(message.getContent(), secretKey);
            message.setContent(decryptedContent);
        }
        return messages;
    }
}
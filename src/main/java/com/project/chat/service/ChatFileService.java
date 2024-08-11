package com.project.chat.service;

import com.project.chat.model.ChatFile;
import com.project.chat.repository.ChatFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChatFileService {

    private final ChatFileRepository chatFileRepository;

    @Autowired
    public ChatFileService(ChatFileRepository chatFileRepository) {
        this.chatFileRepository = chatFileRepository;
    }

    public ChatFile saveFile(MultipartFile file, String sender) throws Exception {
        ChatFile chatFile = ChatFile.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .data(file.getBytes())
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .build();
        return chatFileRepository.save(chatFile);
    }

    public ChatFile getFile(UUID id) {
        return chatFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id " + id));
    }

}
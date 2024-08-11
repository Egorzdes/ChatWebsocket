package com.project.chat.controller;

import com.project.chat.model.ChatFile;
import com.project.chat.service.ChatFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat/files")
public class ChatFileController {

    private final ChatFileService chatFileService;

    @Autowired
    public ChatFileController(ChatFileService chatFileService) {
        this.chatFileService = chatFileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("sender") String sender) throws Exception {
        ChatFile chatFile = chatFileService.saveFile(file, sender);
        return ResponseEntity.ok(chatFile.getId().toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) { // Используйте UUID вместо Long
        ChatFile chatFile = chatFileService.getFile(id);
        if (chatFile != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(chatFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + chatFile.getFileName() + "\"")
                    .body(chatFile.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
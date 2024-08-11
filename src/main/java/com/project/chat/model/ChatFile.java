package com.project.chat.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "ChatFile")
@Builder
@AllArgsConstructor
public class ChatFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    private String sender;
    private LocalDateTime timestamp;

    public ChatFile() {
        this.id = UUID.randomUUID();
    }
}
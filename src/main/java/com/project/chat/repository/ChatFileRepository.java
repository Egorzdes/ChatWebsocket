package com.project.chat.repository;

import com.project.chat.model.ChatFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatFileRepository extends JpaRepository<ChatFile, UUID> {
}
package com.project.chat.service;

import com.project.chat.model.ChatMessage;
import com.project.chat.repository.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    void saveMessage() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        ChatMessage message = new ChatMessage("sender", "Hello World");
        when(chatMessageRepository.save(message)).thenReturn(message);

        // Act
        ChatMessage savedMessage = chatMessageService.saveMessage("sender", "Hello World");

        // Assert
        assertEquals("sender", savedMessage.getSender());
        assertEquals("Hello World", savedMessage.getContent());
        assertEquals(now, savedMessage.getTimestamp());
    }

    @Test
    void getAllMessages() throws Exception {
        // Arrange
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now().plusMinutes(1);
        ChatMessage message1 = new ChatMessage("sender1", "Hello 1");
        ChatMessage message2 = new ChatMessage("sender2", "Hello 2");
        List<ChatMessage> messages = Arrays.asList(message1, message2);
        when(chatMessageRepository.findAll()).thenReturn(messages);

        // Act
        List<ChatMessage> retrievedMessages = chatMessageService.getAllMessages();

        // Assert
        assertEquals(2, retrievedMessages.size());
        assertEquals("Hello 1", retrievedMessages.get(0).getContent());
        assertEquals("Hello 2", retrievedMessages.get(1).getContent());
        assertEquals(now1, retrievedMessages.get(0).getTimestamp());
        assertEquals(now2, retrievedMessages.get(1).getTimestamp());
    }
}

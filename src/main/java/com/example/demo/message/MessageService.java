package com.example.demo.message;

import com.example.demo.presence.PresenceService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // <--- Não esqueça deste import
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final PresenceService presenceService;
    private final UserRepository userRepository;

    public Message sendMessage(User sender, User receiver, String content) {
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        Message savedMessage = messageRepository.save(message);

        if (presenceService.isUserOnline(receiver.getId())) {
            System.out.println("⚡ Usuário " + receiver.getUsername() + " está ONLINE.");
        }

        return savedMessage;
    }

    public List<Message> getHistory(UUID userAId, UUID userBId) {
        User userA = userRepository.findById(userAId)
                .orElseThrow(() -> new RuntimeException("Usuário A não encontrado"));

        User userB = userRepository.findById(userBId)
                .orElseThrow(() -> new RuntimeException("Usuário B não encontrado"));

        return getHistory(userA, userB);
    }

    public List<Message> getHistory(User userA, User userB) {
        return messageRepository.findConversation(userA, userB);
    }
}
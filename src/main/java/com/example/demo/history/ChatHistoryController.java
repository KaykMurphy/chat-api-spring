package com.example.demo.history;

import com.example.demo.message.Message;
import com.example.demo.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final MessageService messageService;

    @GetMapping("/history")
    public ResponseEntity<List<Message>> getHistory(
            @RequestParam UUID userAId,
            @RequestParam UUID userBId
    ) {
        List<Message> history = messageService.getHistory(userAId, userBId);

        return ResponseEntity.ok(history);
    }
}
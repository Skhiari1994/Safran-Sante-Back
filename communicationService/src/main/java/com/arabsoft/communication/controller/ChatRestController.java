package com.arabsoft.communication.controller;

import com.arabsoft.communication.dto.ChatMessageDTO;
import com.arabsoft.communication.dto.ChatRoomDTO;
import com.arabsoft.communication.dto.UserInfoDTO;
import com.arabsoft.communication.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<ChatRoomDTO>> getUserChatRooms(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(chatService.getUserChatRooms(userId));
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestBody ChatMessageDTO messageDTO) {
        return ResponseEntity.ok(chatService.processAndSendMessage(messageDTO));
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageDTO>> getRoomMessages(@PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(chatService.getRoomMessages(roomId));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<ChatMessageDTO>> getUnreadMessages(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(chatService.getUnreadMessages(userId));
    }

    @GetMapping("/unread/count/{userId}")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(chatService.getUnreadCount(userId));
    }

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("messageId") Long messageId) {
        chatService.markMessageAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rooms/{roomId}/read/{userId}")
    public ResponseEntity<Void> markRoomAsRead(@PathVariable("roomId") String roomId,
                                               @PathVariable("userId") String userId) {
        chatService.markRoomAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{user1Id}/{user2Id}")
    public ResponseEntity<String> getOrCreateRoom(@PathVariable("user1Id") String user1Id,
                                                  @PathVariable("user2Id") String user2Id) {
        String roomId = chatService.getOrCreateRoom(user1Id, user2Id);
        return ResponseEntity.ok(roomId);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        return ResponseEntity.ok(chatService.getAllUsers());
    }

    @GetMapping("/rooms/{userId}/recent")
    public ResponseEntity<List<ChatMessageDTO>> getRecentConversations(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(chatService.getRecentConversations(userId));
    }

    @PutMapping("/message/{messageId}/reaction")
    public ResponseEntity<Void> reactToMessage(@PathVariable("messageId") Long messageId,
                                               @RequestParam("userId") String userId,
                                               @RequestParam("emoji") String emoji) {
        chatService.reactToMessage(messageId, userId, emoji);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") Long messageId,
                                              @RequestParam("userId") String userId) {
        chatService.deleteMessage(messageId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/messages/{roomId}")
    public ResponseEntity<Void> deleteRoomMessages(@PathVariable("roomId") String roomId) {
        chatService.deleteRoomMessages(roomId);
        return ResponseEntity.ok().build();
    }
}

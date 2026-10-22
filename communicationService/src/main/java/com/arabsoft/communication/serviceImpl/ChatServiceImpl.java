package com.arabsoft.communication.serviceImpl;

import com.arabsoft.communication.Entities.AdmUser;
import com.arabsoft.communication.Entities.ChatMessage;

import com.arabsoft.communication.Entities.ChatReaction;

import com.arabsoft.communication.Entities.ChatRoom;
import com.arabsoft.communication.Entities.MessageType;
import com.arabsoft.communication.dto.ChatMessageDTO;
import com.arabsoft.communication.dto.ChatRoomDTO;
import com.arabsoft.communication.dto.ReactionDTO;

import com.arabsoft.communication.dto.UserInfoDTO;
import com.arabsoft.communication.mapper.ChatMapper;
import com.arabsoft.communication.repository.AdmUserRepository;
import com.arabsoft.communication.repository.ChatMessageRepository;
import com.arabsoft.communication.repository.ChatReactionRepository;

import com.arabsoft.communication.repository.ChatRoomRepository;
import com.arabsoft.communication.service.ChatService;
import com.arabsoft.communication.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    private final AdmUserRepository admUserRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PresenceService presenceService;
    private final JdbcTemplate jdbcTemplate;

    private final ChatReactionRepository reactionRepository;


    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    @Transactional
    public ChatMessageDTO processAndSendMessage(ChatMessageDTO messageDTO) {
        if (messageDTO.getTimestamp() == null) {
            messageDTO.setTimestamp(LocalDateTime.now());
        }

        // If roomId is missing but receiver is known, get or create the room
        if ((messageDTO.getRoomId() == null || messageDTO.getRoomId().trim().isEmpty())
                && messageDTO.getReceiverId() != null && !messageDTO.getReceiverId().trim().isEmpty()) {
            String roomId = getOrCreateRoom(messageDTO.getSenderId(), messageDTO.getReceiverId());
            messageDTO.setRoomId(roomId);
        }

        boolean isPersistable = messageDTO.getMessageType() == MessageType.CHAT
                || messageDTO.getMessageType() == MessageType.FILE
                || messageDTO.getMessageType() == MessageType.IMAGE
                || messageDTO.getMessageType() == MessageType.VOICE;

        ChatMessageDTO dispatchedMessage = messageDTO;

        if (isPersistable) {
            ChatMessage entity = ChatMapper.toEntity(messageDTO);
            ChatMessage saved = messageRepository.save(entity);

            // Update room's lastMessageAt
            if (messageDTO.getRoomId() != null) {
                roomRepository.findByRoomId(messageDTO.getRoomId()).ifPresent(room -> {
                    room.setLastMessageAt(LocalDateTime.now());
                    roomRepository.save(room);
                });
            }

            dispatchedMessage = ChatMapper.toDTO(saved);
        }

        // Send via WebSocket STOMP
        if (dispatchedMessage.getReceiverId() != null && !dispatchedMessage.getReceiverId().trim().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/messages/" + dispatchedMessage.getReceiverId(), dispatchedMessage);
            log.info("Dispatched message from {} to /topic/messages/{}", dispatchedMessage.getSenderId(), dispatchedMessage.getReceiverId());
        } else {
            messagingTemplate.convertAndSend("/topic/public", dispatchedMessage);
            log.info("Dispatched broadcast message from {} to /topic/public", dispatchedMessage.getSenderId());
        }

        return dispatchedMessage;
    }

    @Override
    @Transactional
    public String getOrCreateRoom(String user1Id, String user2Id) {
        if (user1Id == null || user2Id == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }

        Optional<ChatRoom> existingRoom = roomRepository.findRoomBetweenUsers(user1Id, user2Id);
        if (existingRoom.isPresent()) {
            return existingRoom.get().getRoomId();
        }

        // Create deterministic unique room identifier: room_min_max
        String[] sortedUsers = new String[]{user1Id, user2Id};
        Arrays.sort(sortedUsers);
        String generatedRoomId = "room_" + sortedUsers[0] + "_" + sortedUsers[1];

        // Double check by generated room id
        Optional<ChatRoom> roomById = roomRepository.findByRoomId(generatedRoomId);
        if (roomById.isPresent()) {
            return roomById.get().getRoomId();
        }

        ChatRoom newRoom = ChatRoom.builder()
                .roomId(generatedRoomId)
                .user1Id(user1Id)
                .user2Id(user2Id)
                .createdAt(LocalDateTime.now())
                .lastMessageAt(LocalDateTime.now())
                .build();

        roomRepository.save(newRoom);
        log.info("Created new ChatRoom: {} between {} and {}", generatedRoomId, user1Id, user2Id);
        return generatedRoomId;
    }

    @Override
    public List<ChatMessageDTO> getRoomMessages(String roomId) {
        List<ChatMessage> entities = messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        return ChatMapper.toDTOList(entities);
        List<ChatMessageDTO> dtos = ChatMapper.toDTOList(entities);

        // réactions de tous les messages de la conversation (une seule requête)
        List<Long> ids = dtos.stream().map(ChatMessageDTO::getId).filter(Objects::nonNull).toList();
        if (!ids.isEmpty()) {
            Map<Long, List<ReactionDTO>> byMessage = new HashMap<>();
            for (ChatReaction r : reactionRepository.findByMessageIdIn(ids)) {
                byMessage.computeIfAbsent(r.getMessageId(), k -> new ArrayList<>())
                        .add(new ReactionDTO(r.getUserId(), decodeEmoji(r.getEmoji())));
            }
            dtos.forEach(d -> d.setReactions(byMessage.getOrDefault(d.getId(), new ArrayList<>())));
        }
        return dtos;
    }

    @Override
    public List<ChatRoomDTO> getUserChatRooms(String userId) {
        List<ChatRoom> rooms = roomRepository.findAllUserRooms(userId);
        List<ChatRoomDTO> roomDTOs = new ArrayList<>();

        for (ChatRoom room : rooms) {
            String interlocutorId = room.getUser1Id().equals(userId) ? room.getUser2Id() : room.getUser1Id();
            String interlocutorName = getAdherentDisplayName(interlocutorId);
            ChatMessage latest = messageRepository.findLatestMessageByRoomId(room.getRoomId());
            int unread = messageRepository.countByRoomIdAndReceiverIdAndIsReadFalse(room.getRoomId(), userId);
            boolean isOnline = presenceService.isUserOnline(interlocutorId);
            LocalDateTime lastSeen = presenceService.getLastSeen(interlocutorId);

            ChatRoomDTO dto = ChatRoomDTO.builder()
                    .id(room.getId())
                    .roomId(room.getRoomId())
                    .user1Id(room.getUser1Id())
                    .user2Id(room.getUser2Id())
                    .createdAt(room.getCreatedAt())
                    .lastMessageAt(room.getLastMessageAt())
                    .interlocutorId(interlocutorId)
                    .interlocutorName(interlocutorName)
                    .lastMessage(latest != null ? latest.getContent() : null)
                    .unreadCount(unread)
                    .isOnline(isOnline)
                    .interlocutorLastSeen(lastSeen)
                    .build();

            roomDTOs.add(dto);
        }

        return roomDTOs;
    }

    @Override
    public List<ChatMessageDTO> getRecentConversations(String userId) {
        List<ChatRoom> rooms = roomRepository.findAllUserRooms(userId);
        List<ChatMessageDTO> recents = new ArrayList<>();

        for (ChatRoom room : rooms) {
            ChatMessage latest = messageRepository.findLatestMessageByRoomId(room.getRoomId());
            String otherUserId = room.getUser1Id().equals(userId) ? room.getUser2Id() : room.getUser1Id();

            if (latest != null) {
                int unread = messageRepository.countByRoomIdAndReceiverIdAndIsReadFalse(room.getRoomId(), userId);
                boolean isOnline = presenceService.isUserOnline(otherUserId);
                LocalDateTime lastSeen = presenceService.getLastSeen(otherUserId);
                String otherUserName = getAdherentDisplayName(otherUserId);

                ChatMessageDTO summary = ChatMessageDTO.builder()
                        .id(latest.getId())
                        .roomId(room.getRoomId())
                        .senderId(otherUserId)
                        .senderName(otherUserName)
                        .receiverId(userId)
                        .content(latest.getContent())
                        .messageType(latest.getMessageType())
                        .timestamp(latest.getTimestamp())
                        .isRead(latest.getIsRead())
                        .unreadCount(unread)
                        .isOnline(isOnline)
                        .lastSeen(lastSeen)
                        .build();

                recents.add(summary);
            }
        }

        return recents;
    }

    @Override
    @Transactional
    public void markRoomAsRead(String roomId, String userId) {

        messageRepository.markRoomMessagesAsRead(roomId, userId);
        messageRepository.markRoomMessagesAsRead(roomId, userId, LocalDateTime.now());

        // Notify interlocutor of read receipt via WebSocket
        roomRepository.findByRoomId(roomId).ifPresent(room -> {
            String interlocutorId = room.getUser1Id().equals(userId) ? room.getUser2Id() : room.getUser1Id();
            ChatMessageDTO receipt = ChatMessageDTO.builder()
                    .roomId(roomId)
                    .senderId(userId)
                    .receiverId(interlocutorId)
                    .messageType(MessageType.READ_RECEIPT)
                    .timestamp(LocalDateTime.now())
                    .build();
            messagingTemplate.convertAndSend("/topic/messages/" + interlocutorId, receipt);
            log.info("Sent READ_RECEIPT for room {} to /topic/messages/{}", roomId, interlocutorId);
        });
    }

    @Override
    @Transactional
    public void markMessageAsRead(Long messageId) {
        messageRepository.markMessageAsRead(messageId);
        messageRepository.markMessageAsRead(messageId, LocalDateTime.now());
    }

    private static String encodeEmoji(String emoji) {
        if (emoji == null) {
            return "";
        }
        return emoji.codePoints().mapToObj(Integer::toHexString).collect(java.util.stream.Collectors.joining("-"));
    }

    private static String decodeEmoji(String code) {
        if (code == null || code.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String part : code.split("-")) {
            try {
                sb.appendCodePoint(Integer.parseInt(part, 16));
            } catch (NumberFormatException ignored) {
                // code inconnu : ignoré
            }
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public void reactToMessage(Long messageId, String userId, String emoji) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message introuvable"));
        boolean isParticipant = userId != null
                && (userId.equals(message.getSenderId()) || userId.equals(message.getReceiverId()));
        boolean isGroup = message.getReceiverId() == null || message.getReceiverId().trim().isEmpty();
        if (!isParticipant && !isGroup) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne participez pas à cette conversation");
        }

        // la base Oracle ne conserve pas les émojis : on stocke leurs codes (ex. « 2764-fe0f »)
        String code = encodeEmoji(emoji);
        if (code.isEmpty() || code.length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Émoji invalide");
        }

        // même émoji une seconde fois = retirer ; autre émoji = remplacer
        String result = emoji;
        Optional<ChatReaction> existing = reactionRepository.findByMessageIdAndUserId(messageId, userId);
        if (existing.isPresent()) {
            if (existing.get().getEmoji().equals(code)) {
                reactionRepository.delete(existing.get());
                result = "";
            } else {
                existing.get().setEmoji(code);
                reactionRepository.save(existing.get());
            }
        } else {
            reactionRepository.save(ChatReaction.builder().messageId(messageId).userId(userId).emoji(code).build());
        }

        String other = userId.equals(message.getSenderId()) ? message.getReceiverId() : message.getSenderId();
        ChatMessageDTO notice = ChatMessageDTO.builder()
                .id(messageId)
                .roomId(message.getRoomId())
                .senderId(userId)
                .receiverId(other)
                .content(result)
                .messageType(MessageType.REACTION)
                .timestamp(LocalDateTime.now())
                .build();
        if (other != null && !other.trim().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/messages/" + other, notice);
        } else {
            messagingTemplate.convertAndSend("/topic/public", notice);
        }
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId, String userId) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message introuvable"));
        if (userId == null || !userId.equals(message.getSenderId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul l'expéditeur peut supprimer son message");
        }
        reactionRepository.deleteByMessageId(messageId);
        messageRepository.delete(message);

        ChatMessageDTO notice = ChatMessageDTO.builder()
                .id(message.getId())
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .messageType(MessageType.DELETE)
                .timestamp(LocalDateTime.now())
                .build();
        if (message.getReceiverId() != null && !message.getReceiverId().trim().isEmpty()) {
            messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverId(), notice);
        } else {
            messagingTemplate.convertAndSend("/topic/public", notice);
        }
        log.info("Deleted message {} by {}", messageId, userId);
    }

    @Override
    public int getUnreadCount(String userId) {
        return messageRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    @Override
    public List<ChatMessageDTO> getUnreadMessages(String userId) {
        List<ChatMessage> entities = messageRepository.findByReceiverIdAndIsReadFalse(userId);
        return ChatMapper.toDTOList(entities);
    }

    @Override
    @Transactional
    public void deleteRoomMessages(String roomId) {
        messageRepository.deleteByRoomId(roomId);
        log.info("Deleted all messages for room: {}", roomId);
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {
        List<UserInfoDTO> users = new ArrayList<>();

        // 1. Fetch users from ADMUSER repository
        try {
            List<AdmUser> admUsers = admUserRepository.findAll();
            if (!admUsers.isEmpty()) {
                for (AdmUser u : admUsers) {
                    String id = u.getLogin();
                    if (id == null || id.trim().isEmpty()) {
                        id = u.getMatricule();
                    }
                    if (id == null && u.getUseId() != null) {
                        id = String.valueOf(u.getUseId());
                    }
                    if (id == null) {
                        continue;
                    }

                    boolean isOnline = presenceService.isUserOnline(id);
                    LocalDateTime lastSeenDt = presenceService.getLastSeen(id);
                    String lastSeenStr = (lastSeenDt != null) ? lastSeenDt.format(TIME_FORMATTER) : null;

                    users.add(UserInfoDTO.builder()
                            .id(id)
                            .username(u.getLogin() != null ? u.getLogin() : id)
                            .firstName(u.getFirstName())
                            .lastName(u.getLastName())
                            .email(null)
                            .isOnline(isOnline)
                            .lastSeen(lastSeenStr)
                            .build());
                }
                return users;
            }
        } catch (Exception e) {
            log.error("Error retrieving users from AdmUserRepository: {}", e.getMessage());
        }

        // 2. Fallback: query PERSONNEL if ADMUSER is not populated
        try {
            String sql = "SELECT MAT_PERS, NOM_PERS, PREN_PERS FROM PERSONNEL WHERE ROWNUM <= 200";
            users = jdbcTemplate.query(sql, (rs, rowNum) -> {
                String mat = rs.getString("MAT_PERS");
                String nom = rs.getString("NOM_PERS");
                String pren = rs.getString("PREN_PERS");

                boolean isOnline = presenceService.isUserOnline(mat);
                LocalDateTime lastSeenDt = presenceService.getLastSeen(mat);
                String lastSeenStr = (lastSeenDt != null) ? lastSeenDt.format(TIME_FORMATTER) : null;

                return UserInfoDTO.builder()
                        .id(mat)
                        .username(mat)
                        .firstName(pren)
                        .lastName(nom)
                        .email(null)
                        .isOnline(isOnline)
                        .lastSeen(lastSeenStr)
                        .build();
            });
        } catch (Exception e) {
            log.debug("Personnel query fallback bypassed: {}", e.getMessage());
        }

        return users;
    }

    @Override
    public String getAdherentDisplayName(String userId) {
        if (userId == null) {
            return "";
        }

        // 1. Try via AdmUserRepository
        try {
            Optional<AdmUser> userByLogin = admUserRepository.findByLogin(userId);
            if (userByLogin.isPresent()) {
                AdmUser u = userByLogin.get();
                if (u.getFirstName() != null && u.getLastName() != null) {
                    return (u.getFirstName() + " " + u.getLastName()).trim();
                } else if (u.getFirstName() != null) {
                    return u.getFirstName().trim();
                }
                return u.getLogin();
            }

            Optional<AdmUser> userByMat = admUserRepository.findByMatricule(userId);
            if (userByMat.isPresent()) {
                AdmUser u = userByMat.get();
                if (u.getFirstName() != null && u.getLastName() != null) {
                    return (u.getFirstName() + " " + u.getLastName()).trim();
                }
                return u.getMatricule();
            }
        } catch (Exception ignored) {
        }

        // 2. Try PERSONNEL fallback
        try {
            String sql = "SELECT NOM_PERS, PREN_PERS FROM PERSONNEL WHERE MAT_PERS = ?";
            List<String> names = jdbcTemplate.query(sql, (rs, rowNum) -> {
                String nom = rs.getString("NOM_PERS");
                String pren = rs.getString("PREN_PERS");
                if (pren != null && nom != null) {
                    return (pren + " " + nom).trim();
                } else if (nom != null) {
                    return nom.trim();
                }
                return null;
            }, userId);

            if (!names.isEmpty() && names.get(0) != null && !names.get(0).trim().isEmpty()) {
                return names.get(0);
            }
        } catch (Exception ignored) {
        }

        return userId;
    }
}

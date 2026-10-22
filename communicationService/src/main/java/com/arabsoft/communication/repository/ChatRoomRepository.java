package com.arabsoft.communication.repository;

import com.arabsoft.communication.Entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomId(String roomId);

    @Query("SELECT r FROM ChatRoom r WHERE (r.user1Id = :u1 AND r.user2Id = :u2) OR (r.user1Id = :u2 AND r.user2Id = :u1)")
    Optional<ChatRoom> findRoomBetweenUsers(@Param("u1") String u1, @Param("u2") String u2);

    @Query("SELECT r FROM ChatRoom r WHERE r.user1Id = :userId OR r.user2Id = :userId ORDER BY r.lastMessageAt DESC")
    List<ChatRoom> findAllUserRooms(@Param("userId") String userId);
}

package com.arabsoft.communication.repository;

import com.arabsoft.communication.Entities.ChatReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatReactionRepository extends JpaRepository<ChatReaction, Long> {

    List<ChatReaction> findByMessageIdIn(Collection<Long> messageIds);

    Optional<ChatReaction> findByMessageIdAndUserId(Long messageId, String userId);

    @Transactional
    void deleteByMessageId(Long messageId);
}

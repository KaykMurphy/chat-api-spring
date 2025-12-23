package com.example.demo.message;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :userA AND m.receiver = :userB) OR " +
            "(m.sender = :userB AND m.receiver = :userA) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findConversation(@Param("userA") User userA, @Param("userB") User userB);
}

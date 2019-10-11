package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    @Query("FROM" +
            "   ChatMessage m " +
            "WHERE" +
            "   m.fromUser IN (:user1, :user2)" +
            "AND" +
            "   m.toUser IN (:user1, :user2)")
    List<ChatMessage> findAllByFromUserAndToUser(@Param("user1") User user1, @Param("user2") User user2);

    List<ChatMessage> findByToUserOrFromUser(User toUser, User fromUser);

}

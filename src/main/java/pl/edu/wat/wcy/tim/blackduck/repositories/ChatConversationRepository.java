package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.ChatConversation;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.List;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, String> {

    @Query("FROM" +
            "   ChatConversation c " +
            "WHERE" +
            "   c.user1 IN (:user1, :user2)" +
            "AND" +
            "   c.user2 IN (:user1, :user2)")
    ChatConversation findByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);

    List<ChatConversation> findByUser1OrUser2(User user1, User user2);

}

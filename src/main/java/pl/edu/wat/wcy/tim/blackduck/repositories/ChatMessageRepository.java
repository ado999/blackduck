package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.ChatMessage;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByCidOrderByDateAsc(String cid);
    List<ChatMessage> findByToUserOrFromUser(User toUser, User fromUser);

}

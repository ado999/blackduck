package pl.edu.wat.wcy.tim.blackduck.messaging.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.messaging.models.ChatMessage;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, String> {

    @Query(" FROM"
            + "    ChatMessage m"
            + "  WHERE"
            + "    m.authorUser.id IN (:userIdOne, :userIdTwo)"
            + "  AND"
            + "    m.recipientUser.id IN (:userIdOne, :userIdTwo)"
            + "  ORDER BY"
            + "    m.timeSent"
            + "  DESC")
    List<ChatMessage> getExistingChatMessages(
            @Param("userIdOne") int userIdOne, @Param("userIdTwo") int userIdTwo, Pageable pageable
    );

}

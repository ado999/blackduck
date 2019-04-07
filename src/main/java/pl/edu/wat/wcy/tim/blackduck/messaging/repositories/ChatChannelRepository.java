package pl.edu.wat.wcy.tim.blackduck.messaging.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.wcy.tim.blackduck.messaging.models.ChatChannel;

import java.util.List;

@Transactional
@Repository
public interface ChatChannelRepository extends CrudRepository<ChatChannel, String> {

    @Query(" FROM"
            + "    ChatChannel c"
            + "  WHERE"
            + "    c.userOne.id IN (:userOneId, :userTwoId) "
            + "  AND"
            + "    c.userTwo.id IN (:userOneId, :userTwoId)")
    public List<ChatChannel> findExistingChannel(
            @Param("userOneId") int userOneId, @Param("userTwoId") int userTwoId);

    @Query(" SELECT"
            + "    uuid"
            + "  FROM"
            + "    ChatChannel c"
            + "  WHERE"
            + "    c.userOne.id IN (:userIdOne, :userIdTwo)"
            + "  AND"
            + "    c.userTwo.id IN (:userIdOne, :userIdTwo)")
    public String getChannelUuid(
            @Param("userIdOne") int userIdOne, @Param("userIdTwo") int userIdTwo);

    @Query(" FROM"
            + "    ChatChannel c"
            + "  WHERE"
            + "    c.uuid = :uuid")
    public ChatChannel getChannelDetails(@Param("uuid") String uuid);

}

package mon.sinamon.repository;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ChatroomRepository {

    @PersistenceContext
    private EntityManager em;

    //채팅방 저장
    public Long save(Chatroom chatroom){
        em.persist(chatroom);
        return chatroom.getId();
    }

}

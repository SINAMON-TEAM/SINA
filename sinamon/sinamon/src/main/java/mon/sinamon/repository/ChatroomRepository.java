package mon.sinamon.repository;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import mon.sinamon.domain.Post;
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

    //Chatroom_id로 조회
    public Chatroom findOne(Long id){
        return em.find(Chatroom.class, id);
    }

}

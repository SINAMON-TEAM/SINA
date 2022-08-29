package mon.sinamon.repository;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import mon.sinamon.domain.Chatting;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ChatroomRepository {

    @PersistenceContext
    private EntityManager em;

    //채팅방 저장
    public Long save(Chatroom chatroom){
        em.persist(chatroom);
        return chatroom.getId();
    }


    // Chatroom id로 조회
    public Chatroom findOne(Long id){
        return em.find(Chatroom.class, id);
    }

    // post id와 채팅 건 사람 id로 Chatroom 조회
    public Chatroom findByPostAndMember(Long post_id, Long talker_id){
        List<Chatroom> resultList = em.createQuery(
                        "select c from Chatroom c where c.post.id = :id1 AND c.member2.member_id = :id2", Chatroom.class)
                .setParameter("id1", post_id)
                .setParameter("id2", talker_id)
                .getResultList();
        if(resultList.size()!=0) {
            Chatroom result = resultList.get(0);
            return result;
        }
        else return null;
    }

}

package mon.sinamon.repository;


import mon.sinamon.domain.Chatting;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ChattingRepository {

    @PersistenceContext
    private EntityManager em;


    // Chatting 저장
    public Long save(Chatting chatting){
        em.persist(chatting);
        return chatting.getId();
    }


    // Chatting id로 조회
    public Chatting findOne(Long id){
        return em.find(Chatting.class, id);
    }

    // 전체 Chatting 조회
    public List<Chatting> findAll(){
        return em.createQuery("select c from Chatting c", Chatting.class)
                .getResultList();
    }

    // post id와 채팅 건 사람 id로 Chatting 조회
    public List<Chatting> findByPostAndMember(Long post_id, Long talker_id){
        return em.createQuery(
                "select c from Chatting c where c.post.id = :id1 AND c.talker.member_id = :id2", Chatting.class)
                .setParameter("id1", post_id)
                .setParameter("id2", talker_id)
                .getResultList();
    }


}

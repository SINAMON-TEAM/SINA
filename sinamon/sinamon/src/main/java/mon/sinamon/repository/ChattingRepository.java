package mon.sinamon.repository;


import mon.sinamon.domain.Chatting;
import mon.sinamon.domain.Post;
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

    // id 두개로 Chatting 조회
    public List<Chatting> findByMemberId(Long id1, Long id2){
        return em.createQuery(
                "select c from Chatting c where c.member1.member_id = :id1 AND c.member2.member_id = :id2", Chatting.class)
                .setParameter("id1", id1)
                .setParameter("id2", id2)
                .getResultList();
    }


}

package mon.sinamon.repository;

import mon.sinamon.domain.Chatroom;
import mon.sinamon.domain.Chatting;
import mon.sinamon.domain.Likes;
import mon.sinamon.service.LikeService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Likes likes){
        em.persist(likes);
        return likes.getId();
    }

    public Likes findByPostAndMember(Long post_id, Long member_id){

        List<Likes> resultList = em.createQuery(
                        "select l from Likes l where l.post.id = :id1 AND l.member.member_id = :id2", Likes.class)
                .setParameter("id1", post_id)
                .setParameter("id2", member_id)
                .getResultList();
        if(resultList.size()!=0) {
            Likes result = resultList.get(0);
            return result;
        }
        else return null;
    }

    public void remove(Likes like){
        em.remove(like);
    }

}

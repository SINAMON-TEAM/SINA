package mon.sinamon.repository;


import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;


    // Post 저장
    public Long save(Post post){
        em.persist(post);
        return post.getId();
    }


    // Post id로 조회
    public Post findOne(Long id){
        return em.find(Post.class, id);
    }

    //전체 Post 조회
    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    //회원 id로 게시글 검색 (원래는 닉네임으로 검색하는걸 하고 싶었는데 일단 닉네임이 없으니 id로 만듦)
    public List<Post> findByMemberId(String member_id){
        return em.createQuery("select m from Post p where p.member_id = :id", Post.class)
                .setParameter("id", member_id)
                .getResultList();
    }



}

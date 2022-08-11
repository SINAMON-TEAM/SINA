package mon.sinamon.repository;


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

    //회원이 생성한 id로 게시글 검색 (원래는 닉네임으로 검색하는걸 하고 싶었는데 일단 닉네임이 없으니 id로 만듦)
    public List<Post> findByMemberMakingId(String member_id){
        return em.createQuery("select p from Post p where p.member.id = :member_id", Post.class)
                .setParameter("id", member_id)
                .getResultList();
    }

    //회원에게 부여된 id로 게시글 검색
    public List<Post> findByMemberId(Long id){
        return em.createQuery("select p from Post p where p.member.member_id = :id", Post.class)
                .setParameter("id", id)
                .getResultList();
    }





    ///////////////////////////////////////////////////////
    //오류나는 부분
    public List<Post> findAllWithMember() {
        return em.createQuery(
                        "select p from Post p" +
                                " join fetch p.member", Post.class) // 이 줄만 없으면 mem   ber부분만 null이고 나머지 괜찮음
                .getResultList();
    }

}

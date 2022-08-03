package mon.sinamon.repository;

import mon.sinamon.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;



    public Long save(Member member){
        em.persist(member);
        return member.getMember_id();
    }

    /* 원래 영상에는 이거던데 위에처럼 Long으로 한게 테스트때문인가?
    public void save(Member member) {
        em.persist(member);
    }
    */


    //DB에서 생성된 id값으로 회원 조회
    public Member findOne(Long member_id){
        return em.find(Member.class, member_id);
    }

    //전체 회원 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //이름으로 회원 검색
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    //닉네임으로 회원 검색
    public List<Member> findByNickname(String nickname){
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }


    //회원이 정한 id로 회원 조회, 이건 나중에 반환값을 List<Member>말고 Member로 나오게 수정이 필요할듯
    public List<Member> findById(String id){
        return em.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();
    }



}

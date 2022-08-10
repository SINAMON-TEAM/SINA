package mon.sinamon.service;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Member;
import mon.sinamon.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    * 회원가입
    * */
    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getMember_id();
    }

    //회원 전체 조회
    public List<Member> findMembers() {return memberRepository.findAll();}

    //회원 id값으로 조회
    public Member findMemberById(Long member_id) {return memberRepository.findOne(member_id);}
}

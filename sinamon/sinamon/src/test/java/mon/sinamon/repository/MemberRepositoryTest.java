package mon.sinamon.repository;

import mon.sinamon.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    public void testMember() throws Exception{
        //given
        Member member=new Member();
        member.setName("임상우");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember=memberRepository.findOne(saveId);
        //then
        Assertions.assertThat(findMember.getMember_id()).isEqualTo(member.getMember_id());

    }
}
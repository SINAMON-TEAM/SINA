package mon.sinamon.service;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatting;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.repository.ChattingRepository;
import mon.sinamon.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingService {

    private final PostRepository postRepository;
    private final ChattingRepository chattingRepository;

    /*
     * 채팅 작성 및 저장
     * */
    @Transactional
    public Long join(Chatting chatting){
        chattingRepository.save(chatting);
        return chatting.getId();
    }

    // 전체 채팅 내역 조회
    public List<Chatting> findAllChattings() {return chattingRepository.findAll();}

    // 채팅 id로 조회
    public Chatting findChattingById(Long id) {return chattingRepository.findOne(id);}

    //회원 id 두개로 채팅 조회
    public List<Chatting> findChattingByMemberId(Long id1, Long id2) {return chattingRepository.findByMemberId(id1, id2);}
}

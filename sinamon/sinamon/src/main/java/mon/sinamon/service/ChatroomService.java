package mon.sinamon.service;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import mon.sinamon.domain.Chatting;
import mon.sinamon.repository.ChatroomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;


    //채팅방 생성
    @Transactional
    public Long join(Chatroom chatroom){
        chatroomRepository.save(chatroom);
        return chatroom.getId();
    }


    // 채팅방 id로 조회
    public Chatroom findChattingById(Long id) {return chatroomRepository.findOne(id);}

    //post id와 채팅 누른 사람 id로 채팅방 조회
    public Chatroom findChattingByPostAndMember(Long id1, Long id2) {
        return chatroomRepository.findByPostAndMember(id1, id2);}

    public Chatroom findChatroomById(Long id){
        return chatroomRepository.findOne(id);
    }


}



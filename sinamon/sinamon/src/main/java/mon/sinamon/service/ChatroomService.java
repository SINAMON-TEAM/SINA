package mon.sinamon.service;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import mon.sinamon.repository.ChatroomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Chatroom findChatroomById(Long id){
        return chatroomRepository.findOne(id);
    }

}

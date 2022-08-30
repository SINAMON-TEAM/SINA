package mon.sinamon.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import mon.sinamon.domain.Chatting;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.MessageSender;
import mon.sinamon.service.ChatroomService;
import mon.sinamon.service.ChattingService;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.PostService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChattingApiController {

    private final PostService postService;
    private final MemberService memberService;
    private final ChattingService chattingService;
    private final ChatroomService chatroomService;


    // 채팅 메시지 생성.
    @PostMapping("/api/chattings/create")
    public CreateChattingResponse createChatroom(@RequestBody @Valid CreateChattingRequest request) {

        Chatting chatting = new Chatting();

        chatting.setPost(postService.findPostById(request.getPost()));
        chatting.setPost_writer(memberService.findMemberById(request.getPost_writer()));
        chatting.setTalker(memberService.findMemberById(request.getTalker()));
        chatting.setSender(request.getSender());
        chatting.setMessage(request.getMessage());
        chatting.setMessage_time(LocalDateTime.now());

        Long id = chattingService.join(chatting);
        return new CreateChattingResponse(id);
    }


    // 전체 채팅 내역 조회
    @GetMapping("/api/chattings")
    public List<ChattingDto> getAllChatting() {
        List<Chatting> chattings = chattingService.findAllChattings();
        List<ChattingDto> result = chattings.stream()
                .map(c -> new ChattingDto(c))
                .collect(Collectors.toList());
        return result;
    }



    // 채팅방 생성.
    @PostMapping("/api/chattings/chatroom/create")
    public CreateChatroomResponse createChatroom(@RequestBody @Valid CreateChatroomRequest request) {

        Chatroom chatroom = new Chatroom();
        chatroom.setPost(postService.findPostById(request.getPost()));
        chatroom.setMember1(postService.findPostById(request.getPost()).getMember());
        chatroom.setMember2(memberService.findMemberById(request.getTalker()));

        Long id = chatroomService.join(chatroom);
        return new CreateChatroomResponse(id);
    }



/*

    // post id와 채팅 거는 사람 id로 채팅내역 조회
    @GetMapping("/api/chattings/chattingList")
    public List<ChattingDto> getChattingById(@RequestParam("post") String post, @RequestParam("talker") String talker) {


        Long post_id=Long.valueOf(post);
        Long talker_id=Long.valueOf(talker);

        Long post_writer_id = postService.findPostById(post_id).getMember().getMember_id();

        if(post_writer_id==talker_id){
            System.out.println("당신이 쓴 글입니다");
            return null;
        }

        List<Chatting> chattings = chattingService.findChattingByPostAndMember(post_id, talker_id);
        System.out.println("ff"+chattings.get(0).getMessage());
        List<ChattingDto> result = chattings.stream()
                .map(c->new ChattingDto(c))
                .collect(Collectors.toList());
        return result;
    }
*/




    // 채팅 메시지 저장
    @PostMapping("/api/chattings/save/message")
    public ChattingResponse saveMessage(@RequestBody @Valid ChattingRequest chattingRequest){
        
        Long talker_id= chattingRequest.getTalker_id();
        Long chatroom_id=chattingRequest.getChatroom_id();
        Member talker=memberService.findMemberById(talker_id);
        Chatroom chatroom=chatroomService.findChatroomById(chatroom_id);

        Chatting chatting=new Chatting();
        chatting.setMessage(chattingRequest.message);
        chatting.setSender(chattingRequest.getSender());
        chatting.setChatroom(chatroom);
        chatting.setTalker(talker);

        Long chatting_id = chattingService.join(chatting);

        return new ChattingResponse(chatting_id);

    }


    //채팅방 번호로 채팅조회
    /*
    @PostMapping("api/chatroom/chattings")
    public List<Chatting> getChattingByChatroomId(Long chatroom_id){
        List<Chatting> chattingByChatroomId = chattingService.findChattingByChatroomId(chatroom_id);


        List<ChattingDto> result = chattings.stream()
                .map(c -> new ChattingDto(c))
                .collect(Collectors.toList());
    }*/




    // post id와 채팅 거는 사람 id로 채팅방 조회 & 없으면 생성
    @GetMapping("/api/chattings/chatroom")
    public ChatroomDto getChatroomById(@RequestParam("post") String post, @RequestParam("talker") String talker, HttpServletRequest httpServletRequest) {

        HttpSession session=httpServletRequest.getSession(false);
        if(session==null) System.out.println("xyxy no session");
        Member member = (Member)session.getAttribute("member");
        Long kakao_id=member.getKakao_id();
        Member findMember=memberService.findMemberBykakaoId(kakao_id);

        System.out.println("xyxy "+findMember);


        Long post_id=Long.valueOf(post);
        Long post_writer_id = postService.findPostById(post_id).getMember().getMember_id();
        //Long talker_id=Long.valueOf(talker);
        Long talker_id=findMember.getMember_id();

        if(post_writer_id==talker_id){
            System.out.println("your post");
            return null;
        }

        Chatroom chatroom= chatroomService.findChattingByPostAndMember(post_id, talker_id);
        ChatroomDto result;
        if(chatroom!=null) result = new ChatroomDto(chatroom);
        else{
            CreateChatroomRequest request = new CreateChatroomRequest(post_id,talker_id);
            CreateChatroomResponse newPost = createChatroom(request);
            result = new ChatroomDto(chatroomService.findChattingById(newPost.getId()));

        }
        return result;

    }



    /*******************************여기까지 api 함수 이 아래는 api 함수들이 쓰는 함수들*********************************/


    // 채팅 메시지 저장할때 인자로 받을 클래스

    @Data
    static class ChattingRequest {


        private Long talker_id;

        private MessageSender sender;
        private String message;
        private Long chatroom_id;


    }

    // 채팅메시지 저장하고 반환할 클래스
    @Data
    static class ChattingResponse {
        private Long id;

        public ChattingResponse(Long id) {
            this.id = id;
        }
    }




    @Data
    @AllArgsConstructor
    static class ChattingDto{
        private Long id;
        private Long post_id;
        private Long post_writer_id;
        private Long talker_id;

        private MessageSender sender;
        private String message;
        private LocalDateTime message_time;

        public ChattingDto(Chatting chatting){
            id = chatting.getId();
            post_id=chatting.getPost().getId();
            post_writer_id =chatting.getPost_writer().getMember_id();
            talker_id =chatting.getTalker().getMember_id();
            sender=chatting.getSender();
            message=chatting.getMessage();
            message_time=chatting.getMessage_time();

        }

    }


    // 게시글 작성 api 함수가 인자로 받을 클래스
    @Data
    static class CreateChattingRequest {

        private Long post;

        private Long post_writer;

        private Long talker;

        private MessageSender sender;

        private String message;
    }

    // 게시글 작성 api가 반환하는 클래스, 지금은 id만 반환하도록 하고 있으나 이후에 수정 가능
    @Data
    static class CreateChattingResponse {
        private Long id;

        public CreateChattingResponse(Long id) {
            this.id = id;
        }
    }



    // 채팅 내역 조회용 DTO, 나중에 수정하거나 여러개 만들어서 사용할 것
    @Data
    @AllArgsConstructor
    static class ChatroomDto{
        private Long id;
        private Long post_id;
        private Long post_writer_id;
        private Long talker_id;

        public ChatroomDto(Chatroom chatroom){
            id=chatroom.getId();
            post_id=chatroom.getPost().getId();
            post_writer_id=chatroom.getMember1().getMember_id();
            talker_id=chatroom.getMember2().getMember_id();

        }

    }


    @Data
    @AllArgsConstructor
    static class CreateChatroomRequest {

        private Long post;

        private Long talker;
    }

    @Data
    static class CreateChatroomResponse {
        private Long id;

        public CreateChatroomResponse(Long id) {
            this.id = id;
        }
    }



}
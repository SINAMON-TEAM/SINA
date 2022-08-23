package mon.sinamon.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatting;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.service.ChattingService;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.PostService;
import org.springframework.web.bind.annotation.*;

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


    // 채팅 메시지 생성
    @PostMapping("/api/chattings/create")
    public CreateChattingResponse createChatting(@RequestBody @Valid CreateChattingRequest request) {

        System.out.println(request.getPost());
        System.out.println(request.getMember1());
        System.out.println(request.getMember2());
        System.out.println(request.getMessage());

        Chatting chatting = new Chatting();

        chatting.setPost(postService.findPostById(request.getPost()));
        chatting.setMember1(memberService.findMemberById(request.getMember1()));
        chatting.setMember2(memberService.findMemberById(request.getMember2()));
        chatting.setMessage(request.getMessage());
        chatting.setMessage_time(LocalDateTime.now());

        System.out.println(chatting.getId());
        System.out.println(chatting.getPost().getId());
        System.out.println(chatting.getMember1().getMember_id());
        System.out.println(chatting.getMember2().getMember_id());
        System.out.println(chatting.getMessage());
        System.out.println(chatting.getMessage_time());

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



    // 두 멤버의 id로 채팅내역 조회
    @GetMapping("/api/chattings/chatroom") // url에서 id값을 받아 인자로 활용
    public List<ChattingDto> getChattingById(@RequestParam("member1") String member1, @RequestParam("member2") String member2) {


        Long id1=Long.valueOf(member1);
        Long id2=Long.valueOf(member2);


        List<Chatting> chattings = chattingService.findChattingByMemberId(id1, id2);
        List<ChattingDto> result = chattings.stream()
                .map(c->new ChattingDto(c))
                .collect(Collectors.toList());
        return result;
    }



    /*******************************여기까지 api 함수 이 아래는 api 함수들이 쓰는 함수들*********************************/


    // 채팅 내역 조회용 DTO, 나중에 수정하거나 여러개 만들어서 사용할 것
    @Data
    @AllArgsConstructor
    static class ChattingDto{
        private Long id;
        private Long post_id;
        private Long member1_id;
        private Long member2_id;
        private String message;
        private LocalDateTime message_time;

        public ChattingDto(Chatting chatting){
            id = chatting.getId();
            post_id=chatting.getPost().getId();
            member1_id=chatting.getMember1().getMember_id();
            member2_id=chatting.getMember2().getMember_id();
            message=chatting.getMessage();
            message_time=chatting.getMessage_time();

        }

    }


    // 게시글 작성 api 함수가 인자로 받을 클래스
    @Data
    static class CreateChattingRequest {

        private Long post;

        private Long member1;

        private Long member2;

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


}

package mon.sinamon.api;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.domain.PromiseStatus;
import mon.sinamon.repository.MemberRepository;
import mon.sinamon.repository.PostRepository;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.PostService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


/*
    @PostMapping("/posts/new")
    public void create(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse){
        HttpSession session=httpServletRequest.getSession();
        Member member = (Member) session.getAttribute("member");
        Member memberBykakaoId = memberService.findMemberBykakaoId(member.getKakao_id());
        System.out.println("memberBykakaoId:"+memberBykakaoId.getMember_id());
    */
    // 게시글 작성.
    @PostMapping("/api/posts/create")
    public CreatePostResponse createPost(@RequestBody @Valid CreatePostRequest request,HttpServletRequest httpServletRequest) {


        HttpSession session=httpServletRequest.getSession();
        Member member = (Member) session.getAttribute("member");
        Member memberBykakaoId = memberService.findMemberBykakaoId(member.getKakao_id());

        // request에서 받은 회원정보를 member 객체로 생성
        Post post = new Post();
        post.setMember(memberBykakaoId);
        post.setType(request.getType());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setPromise_time(request.getPromise_time());
        post.setMax_people(request.getMax_people());


        Long id = postService.join(post);
        return new CreatePostResponse(id);
    }


    // 전체 게시글 조회
    @GetMapping("/api/posts")
    public List<PostDto> getAllPost() {
        List<Post> posts = postService.findAllPosts();
        List<PostDto> result = posts.stream()
                .map(p -> new PostDto(p))
                .collect(Collectors.toList());
        return result;
    }


    // 회원 id로 게시글 조회
    @GetMapping("/api/posts/findbymember/{member_id}") // url에서 id값을 받아 인자로 활용
    public List<PostDto> getPostByMemberId(@PathVariable Long member_id) {

        //List<Post> posts = postRepository.findByMemberId(member_id);
        List<Post> posts = postService.findPostByMemberId(member_id);
        List<PostDto> result = posts.stream()
                .map(p -> new PostDto(p))
                .collect(Collectors.toList());
        return result;
    }


    // 오류남 다시생각해야됨
    // 회원이 만든 id로 게시글 조회
//    @GetMapping("/api/posts/findbymemberid/{member_id}") // url에서 id값을 받아 인자로 활용
//    public List<PostDto> getPostByMemberMakingId(@PathVariable String member_id) {
//
//        List<Post> posts = postService.findPostByMemberMakingId(member_id);
//        List<PostDto> result = posts.stream()
//                .map(p -> new PostDto(p))
//                .collect(Collectors.toList());
//        return result;
//    }


    // 게시글 id로 게시글 조회
    @GetMapping("/api/posts/{id}") // url에서 id값을 받아 인자로 활용
    public PostDto getPostById(@PathVariable Long id) {

        Post post = postRepository.findOne(id);
        PostDto result = new PostDto(post);
        return result;
    }


    //약속에 인원 추가
    @PostMapping("/api/posts/addmember")
    public PostDto addMemberToPromise(@RequestParam("postid") String postid, @RequestParam("memberid") String memberid) {

        Long postId=Long.valueOf(postid);
        Long memberId=Long.valueOf(memberid);
        Post post = postService.findPostById(Long.valueOf(postId));
        Member member = memberService.findMemberById(Long.valueOf(memberId));

        ArrayList<Long> array = new ArrayList<Long>(); // 타입 지정

        array.add(post.getMember().getMember_id());
        if(post.getMember2()!=null) array.add(post.getMember2().getMember_id());
        if(post.getMember3()!=null) array.add(post.getMember3().getMember_id());
        if(post.getMember4()!=null) array.add(post.getMember4().getMember_id());
        for(int i=0;i<array.size();i++){
            if(array.get(i)==member.getMember_id()){
                System.out.println("error - duplicate");
                PostDto postDto = new PostDto(post);
                return postDto;
            }
        }


        if (post.getMember2() == null&&post.getMember().getMember_id()!=memberId&&post.getMax_people()>=2) {
            post.setMember2(member);
            post.setNow_people(post.getNow_people() + 1);
        } else if (post.getMember3() == null&&post.getMax_people()>=3&&post.getMember().getMember_id()!=memberId&&post.getMember2().getMember_id()!=memberId) {
            post.setMember3(member);
            post.setNow_people(post.getNow_people() + 1);
        } else if (post.getMember4() == null&&post.getMax_people()>=4&&post.getMember().getMember_id()!=memberId&&post.getMember2().getMember_id()!=memberId&&post.getMember3().getMember_id()!=memberId) {
            post.setMember4(member);
            post.setNow_people(post.getNow_people() + 1);
        } else {
            System.out.println("error - people over");
        }

        if(post.getMax_people()==post.getNow_people()) post.setPromist_status(PromiseStatus.COMP);

        postService.join(post);

        PostDto result = new PostDto(post);
        return result;
    }


    //약속에서 인원 삭제
    @PostMapping("/api/posts/removemember")
    public PostDto removeMemberFromPromise(@RequestParam("postid") String postid, @RequestParam("memberid") String memberid) {

        Post post = postService.findPostById(Long.valueOf(postid));
        Member member = memberService.findMemberById(Long.valueOf(memberid));

        if (post.getMember2() != null && post.getMember2().getMember_id() == Long.valueOf(memberid)) {
            post.setMember2(null);
            post.setNow_people(post.getNow_people() - 1);
        } else if (post.getMember3() != null && post.getMember3().getMember_id() == Long.valueOf(memberid)) {
            post.setMember3(null);
            post.setNow_people(post.getNow_people() - 1);
        } else if (post.getMember4() != null && post.getMember4().getMember_id() == Long.valueOf(memberid)) {
            post.setMember4(null);
            post.setNow_people(post.getNow_people() - 1);
        } else {
            System.out.println("error - not removed");
        }

        if(post.getPromist_status()==PromiseStatus.COMP&&post.getMax_people()!=post.getNow_people())
            post.setPromist_status(PromiseStatus.READY);

        postService.join(post);

        PostDto result = new PostDto(post);
        return result;
    }


    /*******************************여기까지 api 함수 이 아래는 api 함수들이 쓰는 함수들*********************************/


    // 게시글 조회용 DTO, 나중에 수정하거나 여러개 만들어서 사용할 것
    @Data
    @AllArgsConstructor
    static class PostDto {

        private Long id;

        private Long member_id;

        private String type;
        private String title;
        private String text;
        private LocalDateTime post_date;
        private String promise_time;
        private int max_people;
        private int now_people;
        private PromiseStatus promise_status;
        private int view;
        private int like_count;
        private Long member_id2;
        private Long member_id3;
        private Long member_id4;

        public PostDto(Post post) {
            id = post.getId();
            member_id = post.getMember().getMember_id();
            type = post.getType();
            title = post.getTitle();
            text = post.getText();
            post_date = post.getPost_date();
            promise_time = post.getPromise_time();
            max_people = post.getMax_people();
            now_people = post.getNow_people();
            promise_status = post.getPromist_status();
            view = post.getView();
            like_count = post.getLike_count();

            if (post.getMember2() == null) member_id2 = Long.valueOf(0);
            else member_id2 = post.getMember2().getMember_id();
            if (post.getMember3() == null) member_id3 = Long.valueOf(0);
            else member_id3 = post.getMember3().getMember_id();
            if (post.getMember4() == null) member_id4 = Long.valueOf(0);
            else member_id4 = post.getMember4().getMember_id();

        }
    }


    // 게시글 작성 api 함수가 인자로 받을 클래스
    @Data
    static class CreatePostRequest {
        //private Long id;

        private Long member_id;

        private String type;
        private String title;
        private String text;
        private String promise_time;
        private int max_people;
    }

    // 게시글 작성 api가 반환하는 클래스, 지금은 id만 반환하도록 하고 있으나 이후에 수정 가능
    @Data
    static class CreatePostResponse {
        private Long id;

        public CreatePostResponse(Long id) {
            this.id = id;
        }
    }


}

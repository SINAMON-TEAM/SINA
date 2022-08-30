package mon.sinamon.api;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.*;
import mon.sinamon.repository.MemberRepository;
import mon.sinamon.repository.PostRepository;
import mon.sinamon.service.ChatroomService;
import mon.sinamon.service.LikeService;
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
    private final MemberService memberService;
    private final ChatroomService chatroomService;
    private final LikeService likeService;


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


        HttpSession session=httpServletRequest.getSession(false);
        if(session==null){
            System.out.println("로그인이 안됐습니다");
        }

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

        System.out.println("rere"+post.getId());

        Long id = postService.join(post);
        return new CreatePostResponse(id);
    }

    @PostMapping("/api/posts/create2")
    public CreatePostResponse createPost2(@RequestBody @Valid CreatePostRequest request,HttpServletRequest httpServletRequest) {


        HttpSession session=httpServletRequest.getSession();

        Long member_id=(Long)session.getAttribute("member_id");
        Member findMember = memberService.findMemberById(member_id);
        // request에서 받은 회원정보를 member 객체로 생성
        Post post = new Post();
        post.setMember(findMember);
        post.setType(request.getType());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setPromise_time(request.getPromise_time());
        post.setMax_people(request.getMax_people());

        System.out.println("rere"+post.getId());

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

        Post post = postService.findPostById(id);

        //조회수 증가
        postService.updateViewCount(post,post.getView()+1);

        PostDto result = new PostDto(post);
        return result;
    }

    // 게시글 좋아요 누르기
    @PostMapping("/api/posts/{id}/like") // url에서 id값을 받아 인자로 활용
    public void pressLike(@PathVariable Long id, HttpServletRequest httpServletRequest ){
        HttpSession session=httpServletRequest.getSession(false);
        if(session==null){
            System.out.println("로그인이 안됐습니다");
        }

        Member member = (Member) session.getAttribute("member");
        Member memberBykakaoId = memberService.findMemberBykakaoId(member.getKakao_id());

        //게시글 좋아요 수 증가시키기
        Post postById = postService.findPostById(id);
        int like_count = postById.getLike_count();



        //예전에 좋아요 한 상태인지 아닌지 체크
        Likes findLikes = likeService.findLikesByPostAndMember(postById.getId(), memberBykakaoId.getMember_id());
        Likes likes=new Likes();

        if(findLikes==null){        //처음 좋아요를 눌렀으면 db에 저장하고 게시글 증가

            likes.setMember(memberBykakaoId);
            likes.setPost(postById);
            likeService.join(likes);
            likeService.updateLikeCount(postById,like_count+1);
        }
        else{       //좋아요를 두 번 누른거면 db에서 삭제
            likeService.removeLike(findLikes);
            likeService.updateLikeCount(postById,like_count-1);
        }


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

    //게시글 번호랑 채팅건 회원 아이디 넘겨주는 api
    @PostMapping("/api/posts/create/chatroom")
    public void chatroomInfo(@RequestParam String post,HttpServletRequest httpServletRequest) {

        Long post_id=Long.valueOf(post);
        HttpSession session=httpServletRequest.getSession();
        Member findMember = (Member) session.getAttribute("member");
        Member member2 = memberService.findMemberBykakaoId(findMember.getKakao_id());

        Post findPost = postService.findPostById(post_id);
        Member member1 = findPost.getMember();

        Chatroom chatroom=new Chatroom();
        chatroom.setMember1(member1);
        chatroom.setMember2(member2);
        chatroom.setPost(findPost);

        Long id = chatroomService.join(chatroom);



        //return id;
    }

    @PostMapping("/api/posts/create/chatroom2")
    public void chatroomInfo2(@RequestParam String post,HttpServletRequest httpServletRequest) {

        Long post_id=Long.valueOf(post);
        HttpSession session=httpServletRequest.getSession();
        Long member_id=(Long)session.getAttribute("member_id");
        Member member2=memberService.findMemberById(member_id);


        Post findPost = postService.findPostById(post_id);
        Member member1 = findPost.getMember();

        Chatroom chatroom=new Chatroom();
        chatroom.setMember1(member1);
        chatroom.setMember2(member2);
        chatroom.setPost(findPost);

        Long id = chatroomService.join(chatroom);



        //return id;
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

    @Data
    static class ChatroomInfo{
       private Long post_id;
       private Long member_id2;

       public ChatroomInfo(Long post_id, Long member_id2){
           this.post_id=post_id;
           this.member_id2=member_id2;
       }
    }

    // 게시글 작성 api가 반환하는 클래스, 지금은 id만 반환하도록 하고 있으나 이후에 수정 가능
    @Data
    static class CreatePostResponse {
        private Long id;

        public CreatePostResponse(Long id) {
            this.id = id;
        }
    }


    // 좋아요 눌렀을때 반환하는 클래스
    @Data
    static class PressLikeResponse {
        private Long id;

        public PressLikeResponse(Long id) {
            this.id = id;
        }
    }



}

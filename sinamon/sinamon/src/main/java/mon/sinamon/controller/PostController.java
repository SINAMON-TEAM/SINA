package mon.sinamon.controller;

import com.google.gson.JsonElement;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Address;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final MemberService memberService;
    private final PostService postService;


    @GetMapping("/posts/new")
    public String postForm(Model model){
        model.addAttribute("postForm",new PostForm());
        return "posts/createPostForm";
    }

    /*
    //게시글 작성(mvc버전)
    @PostMapping("/posts/new")
    public String create(@Valid PostForm postForm, BindingResult result, HttpSession session){

        String access_Token=(String)session.getAttribute("access_Token");
        System.out.println("아잇 access_Token = " + access_Token);
        JsonElement element = memberService.getJsonElement(access_Token);
        Long kakao_id = element.getAsJsonObject().get("id").getAsLong();
        System.out.println("kakao_id = " + kakao_id);

        Member findMember = memberService.findMemberBykakaoId(kakao_id);
       // System.out.println("findmember id" + findMember.getMember_id());



        Post post=new Post();
        post.setTitle(postForm.getTitle());
        post.setText(postForm.getText());
        post.setType(postForm.getType());
        post.setView(postForm.getView());
        post.setLike_count(post.getLike_count());
        post.setPost_date(LocalDateTime.now());
        post.setMember(findMember);



        Long id = postService.join(post);
        return "redirect:/";
    }
    */

    //게시글 작성(api버전)
    @PostMapping("/posts/new")
    public void create(
                         HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse){
        HttpSession session=httpServletRequest.getSession();
        Member member = (Member) session.getAttribute("member");
        Member memberBykakaoId = memberService.findMemberBykakaoId(member.getKakao_id());
        System.out.println("memberBykakaoId:"+memberBykakaoId.getMember_id());

        /*
        String kaKaoAccessToken = memberService.getKaKaoAccessToken(code);
        JsonElement element = memberService.getJsonElement(kaKaoAccessToken);
        Long id;

        String access_Token=(String)session.getAttribute("access_Token");
        System.out.println("아잇 access_Token = " + access_Token);
        JsonElement element = memberService.getJsonElement(access_Token);
        Long kakao_id = element.getAsJsonObject().get("id").getAsLong();
        System.out.println("kakao_id = " + kakao_id);

        Member findMember = memberService.findMemberBykakaoId(kakao_id);
        // System.out.println("findmember id" + findMember.getMember_id());



        Post post=new Post();
        post.setTitle(postForm.getTitle());
        post.setText(postForm.getText());
        post.setType(postForm.getType());
        post.setView(postForm.getView());
        post.setLike_count(post.getLike_count());
        post.setPost_date(LocalDateTime.now());
        post.setMember(findMember);



        Long id = postService.join(post);
               */

    }


    @Data
    static class CreatePostRequest {
        //private Long id;

        private Long member_id;

        private String type;
        private String title;
        private String text;
        private int view = 0;
        private int like_count = 0;
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

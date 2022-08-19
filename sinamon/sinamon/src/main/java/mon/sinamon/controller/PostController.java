package mon.sinamon.controller;

import com.google.gson.JsonElement;
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
import org.springframework.web.bind.annotation.RestController;

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

}

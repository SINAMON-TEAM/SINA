package mon.sinamon.controller;

import com.google.gson.JsonElement;
import lombok.Data;
import mon.sinamon.controller.MemberForm;
import lombok.RequiredArgsConstructor;
import mon.sinamon.api.MemberApiController;
import mon.sinamon.domain.Address;
import mon.sinamon.domain.Member;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserService userService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        if(result.hasErrors()){
            return "members/createMemberForm";
        }
        Address address=new Address(form.getAddress(), form.getZipcode());
        Member member=new Member();
        member.setAddress(address);
        //member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setName(form.getName());
        member.setPhone(form.getPhone());
        member.setNickname(form.getNickname());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members=memberService.findMembers();
        model.addAttribute("members", members);
        return"members/memberList";
    }


    @GetMapping("/members/kakao")
    public String kakaoLogin(Model model){
        return "members/kakao";
    }



    /*
    @PostMapping("/api/kakao")
    public void createKaKaoMember(@RequestParam String code) {
        System.out.println(code);
        String kaKaoAccessToken = memberService.getKaKaoAccessToken(code);
        JsonElement element = memberService.getJsonElement(kaKaoAccessToken);

        Member member = new Member();

        Long kakao_id = element.getAsJsonObject().get("id").getAsLong();
        boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
        String email = "";
        String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
        if (hasEmail) {
            email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            member.setEmail(email);
        }
        System.out.println("name = " + name);
        System.out.println("email = " + email);
        System.out.println("kakao_id = " + kakao_id);

        member.setId(kakao_id);
        member.setName(name);
        Long id = memberService.join(member);
    }

*/

    @Data
    static class CreateMemberRequest {
        private String id;
        private String password;
        private String name;
        private String phone;
        private String nickname;
        private String address;
        private String zipcode;
    }

    // 회원 가입 api가 반환하는 클래스, 지금은 id만 반환하도록 하고 있으나 이후에 수정 가능
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


}

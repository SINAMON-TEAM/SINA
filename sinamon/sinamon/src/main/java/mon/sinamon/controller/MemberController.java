package mon.sinamon.controller;

import mon.sinamon.controller.MemberForm;
import lombok.RequiredArgsConstructor;
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
        member.setId(form.getId());
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

    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
        String kaKaoAccessToken = userService.getKaKaoAccessToken(code);
        userService.createKakaoUser(kaKaoAccessToken);
    }


}

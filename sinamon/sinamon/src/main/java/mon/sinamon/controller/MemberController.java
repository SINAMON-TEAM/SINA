package mon.sinamon.controller;

import com.google.gson.JsonElement;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Member;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    /*
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
*/
    @GetMapping("/members")
    public String list(Model model, HttpSession session){
        List<Member> members=memberService.findMembers();
        model.addAttribute("members", members);
        System.out.println("session access token"+session.getAttribute("access_Token"));
        return"members/memberList";
    }


    @GetMapping("/members/kakao")
    public String kakaoLogin(Model model){
        return "members/kakao";
    }

/*
    //카카오 회원가입(api버전)
    @PostMapping("/api/kakao")
    public void createKakaoMember(@RequestParam String code
    , HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse) {
        HttpSession session=httpServletRequest.getSession();
        String kaKaoAccessToken = memberService.getKaKaoAccessToken(code);
        JsonElement element = memberService.getJsonElement(kaKaoAccessToken);
        Long id;

        Member member = new Member();

        Long kakao_id = element.getAsJsonObject().get("id").getAsLong();
        boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
        String email = "";
        String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
        if (hasEmail) {
            email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            member.setEmail(email);
            session.setAttribute("email", email);
        }


        member.setKakao_id(kakao_id);
        member.setName(name);

        try {   //카카오 아이디로 회원이 존재하는지 조회
            Member findMember = memberService.findMemberBykakaoId(kakao_id);
            id=findMember.getMember_id();           //회원가입이 돼있으면 회원가입을 따로 안한다
        }
        catch(Exception e){ //회원가입이 안돼있을때
            id = memberService.join(member);    //회원가입을 한다
        }


        session.setAttribute("member", member);
        Cookie authCookie=new Cookie("kakao_id", Long.toString(kakao_id));
        httpServletResponse.addCookie(authCookie);

    }
*/




    //카카오 회원가입(mvc버전)
    @GetMapping("/api/members/kakaologin")
    public void createKakaoMember(@RequestParam String code, HttpServletRequest httpServletRequest) {
        HttpSession session=httpServletRequest.getSession();
        String kaKaoAccessToken = memberService.getKaKaoAccessToken(code);
        JsonElement element = memberService.getJsonElement(kaKaoAccessToken);
        Long id;



        Member member = new Member();

        Long kakao_id = element.getAsJsonObject().get("id").getAsLong();
        boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
        String email = "";
        String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
        if (hasEmail) {
            email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            member.setEmail(email);
            session.setAttribute("email", email);
        }


        member.setKakao_id(kakao_id);
        member.setName(name);

        try {   //카카오 아이디로 회원이 존재하는지 조회
            Member findMember = memberService.findMemberBykakaoId(kakao_id);
            id=findMember.getMember_id();           //회원가입이 돼있으면 회원가입을 따로 안한다
        }
        catch(Exception e){ //회원가입이 안돼있을때
            id = memberService.join(member);    //회원가입을 한다
        }

        session.setAttribute("access_token", kaKaoAccessToken);
        session.setAttribute("member",member);



    }





    @RequestMapping(value="/members/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HttpSession session=httpServletRequest.getSession(false);
        String access_Token=(String)session.getAttribute("access_token");
        System.out.println("accessToken:"+access_Token);
        memberService.kakaoLogout((String)session.getAttribute("access_token"));;
        expireCookie(httpServletResponse);
        return "home";
    }

    private void expireCookie(HttpServletResponse response){
        Cookie cookie=new Cookie("JSESSIONID",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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

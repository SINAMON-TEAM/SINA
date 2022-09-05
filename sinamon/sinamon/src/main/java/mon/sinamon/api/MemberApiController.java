package mon.sinamon.api;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Address;
import mon.sinamon.domain.Member;
import mon.sinamon.service.MemberService;
import mon.sinamon.service.UserService;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Embedded;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


   // 카카오 코드 받기

/*
    @GetMapping("/api/members/kakaologin")
    public String kakaoCallback() {
        https://kauth.kakao.com/oauth/authorize?client_id=5a839021b9022410f2f0a040060fc1dc&redirect_uri=http://localhost:8080/api/members/kakaologin&response_type=code
        return "z ";
    }
*/



/*
    //카카오 로그인,회원가입(api버전)
    @PostMapping("/api/members/kakaologin")
    public void createKakaoMember(@RequestParam String code
            , HttpServletRequest httpServletRequest) {



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
        session.setAttribute("access_token",kaKaoAccessToken);

    }

*/

    @PostMapping("api/members/kakaologout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        HttpSession session=httpServletRequest.getSession(false);
        String access_Token=(String)session.getAttribute("access_token");
        memberService.kakaoLogout(access_Token);
        if(session!=null){
            session.invalidate();
        }
    }




    @PostMapping("/api/members/create")
    public void createMember(@RequestBody @Valid CreateMemberRequest createMemberRequest,
                                             HttpServletRequest httpServletRequest) {

        HttpSession session=httpServletRequest.getSession();
        Member member = (Member) session.getAttribute("member");
        Member memberBykakaoId = memberService.findMemberBykakaoId(member.getKakao_id());
        String major=createMemberRequest.getMajor();
        String address=createMemberRequest.getAddress();
        String nickname=createMemberRequest.getNickname();

        memberService.updateMember(memberBykakaoId.getKakao_id(),major,address,nickname);

    }


    @PostMapping("/api/members/create2")
    public void createMember2(@RequestBody @Valid CreateMemberRequest createMemberRequest,
                              HttpServletRequest httpServletRequest) {

        HttpSession session=httpServletRequest.getSession();



        String major=createMemberRequest.getMajor();
        String address=createMemberRequest.getAddress();
        String nickname=createMemberRequest.getNickname();

        Member member=new Member();
        member.setMajor(major);
        member.setAddress(address);
        member.setNickname(nickname);

        Long member_id = memberService.join(member);
        session.setAttribute("member_id", member_id);


    }



    /*
    // 회원가입
    @PostMapping("/api/members/create")
    public CreateMemberResponse createMember(@RequestBody @Valid CreateMemberRequest request) {

        // request에서 받은 회원정보를 member 객체로 생성
        Member member = new Member();
        member.setKakao_id(request.getKakao_id());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setNickname(request.getNickname());
        Address address = new Address(request.getAddress(), request.getZipcode());
        member.setAddress(address);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
*/

    // id값으로 회원 조회
    @GetMapping("/api/members/{id}") //id값을 url에서 받아와 인자로 활용
    public MemberDto getMemberById(@PathVariable Long id) {
        Member m = memberService.findMemberById(id);
        MemberDto memberDto = new MemberDto(m.getName(), m.getNickname(), m.getAddress(), m.getMajor());
        return memberDto;
    }


    // 전체 회원 조회
    @GetMapping("/api/members")
    public Result getAllMembers() {
        List<Member> findMembers = memberService.findMembers(); // 회원 목록을 List로 받아옴
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName(), m.getNickname(), m.getAddress(), m.getMajor()))
                .collect(Collectors.toList()); //Member -> DTO 변환
        return new Result(collect.size(), collect);
    }


    /*******************************여기까지 api 함수 이 아래는 api 함수들이 쓰는 함수들*********************************/


    // Json 형식으로 반환을 할 때 Json Array를 그대로 반환하기보다는 이렇게 Result라는 틀로 한번 감싸서 반환하는 것이 유연성에 도움이 된다고 함
    // 예시를 위해 Result 클래스의 멤버로 회원 수를 나타내는 변수 count를 만들었음 이런식으로 배열 외에 다른 필요한 정보 함께 반환 가능
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    // 회원 조회용 DTO, 지금은 예제 사이트에 맞춰서 만든 상태이고 나중에 수정하거나 여러개 만들어서 사용할 것
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
        private String nickname;
        private String address;
        private String major;
    }

    /*
    // 회원 가입 api 함수가 인자로 받을 클래스
    @Data
    static class CreateMemberRequest {
        private Long kakao_id;
        private String password;
        private String name;
        private String phone;
        private String nickname;
        private String address;
        private String zipcode;
    }
    */

    @Data
    static class CreateMemberRequest {
        private String major;
        private String address;
        private String nickname;
    }

    // 회원 가입 api가 반환하는 클래스, 지금은 id만 반환하도록 하고 있으나 이후에 수정 가능
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberResponse2 {
        private Long id;
        private String email;
        private Long kakao_id;
        private String name;
        public CreateMemberResponse2(Long id, String email, Long kakao_id, String name) {

            this.id = id;
            this.email=email;
            this.kakao_id=kakao_id;
            this.name=name;
        }
    }
}
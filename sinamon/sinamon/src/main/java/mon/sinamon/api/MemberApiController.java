package mon.sinamon.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Address;
import mon.sinamon.domain.Member;
import mon.sinamon.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Embedded;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/api/members/create")
    public CreateMemberResponse createMember(@RequestBody @Valid CreateMemberRequest request) {

        // request에서 받은 회원정보를 member 객체로 생성
        Member member = new Member();
        member.setId(request.getId());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setNickname(request.getNickname());
        Address address = new Address(request.getAddress(), request.getZipcode());
        member.setAddress(address);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }



    // id값으로 회원 조회
    @GetMapping("/api/members/{id}") //id값을 url에서 받아와 인자로 활용
    public MemberDto getMemberById(@PathVariable Long id) {
        Member m = memberService.findMemberById(id);
        MemberDto memberDto = new MemberDto(m.getName(),m.getPhone(),m.getNickname(),m.getAddress().getAddress(),m.getAddress().getZipcode());
        return memberDto;
    }



    // 전체 회원 조회
    @GetMapping("/api/members")
    public Result getAllMembers() {
        List<Member> findMembers = memberService.findMembers(); // 회원 목록을 List로 받아옴
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName(),m.getPhone(),m.getNickname(),m.getAddress().getAddress(),m.getAddress().getZipcode()))
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
        private String phone;
        private String nickname;
        private String address;
        private String zipcode;

    }

    // 회원 가입 api 함수가 인자로 받을 클래스
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

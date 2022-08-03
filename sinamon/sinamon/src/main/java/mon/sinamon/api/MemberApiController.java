package mon.sinamon.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Address;
import mon.sinamon.domain.Member;
import mon.sinamon.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    // 전체 회원 조회
    @GetMapping("/api/members")
    public Result lookupMember() {
        List<Member> findMembers = memberService.findMembers();
        //엔티티 -> DTO 변환
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName(),m.getPhone(),m.getNickname(),m.getAddress().getAddress(),m.getAddress().getZipcode()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
        private String phone;
        private String nickname;
        private String address;
        private String zipcode;

    }

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
    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}

package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message="회원 아이디는 필수 입니다")
    private String id;

    private String password;
    private String name;
    private String phone;
    private String nickname;

    private String address;
    private String zipcode;

}

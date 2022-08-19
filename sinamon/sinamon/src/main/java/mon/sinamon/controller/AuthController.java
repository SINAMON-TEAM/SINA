package mon.sinamon.controller;

import mon.sinamon.domain.Member;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
@RestController
public class AuthController {
    @PostMapping("/api/auth")
    public void auth(@RequestParam("username") String username,
                     @RequestParam("password") String password,
                     HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) {
        HttpSession session = httpServletRequest.getSession();
        Member member = new Member(username, password);
        session.setAttribute("member", member);
        Cookie authCookie = new Cookie("AUTH", member.getUsername());
        httpServletResponse.addCookie(authCookie);    }

    @GetMapping("/api/profiles")
    public Member getUsers(@CookieValue(value = "AUTH", required = true) Cookie authCookie, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        hasUser(authCookie);
        Member member = (Member) session.getAttribute("member");
        return member;    }

    private void hasUser(Cookie cookie) {
        if (cookie == null) {
            throw new RuntimeException("세션에 유저가 존재하지 않습니다.");
        }
    }
}*/

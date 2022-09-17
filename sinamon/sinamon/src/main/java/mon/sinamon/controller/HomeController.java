package mon.sinamon.controller;


import mon.sinamon.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String hello(HttpServletRequest httpServletRequest,Model model) {
        HttpSession session=httpServletRequest.getSession();
        return "home";
    }

}

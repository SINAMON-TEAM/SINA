package mon.sinamon.controller;


import mon.sinamon.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin("*") // 모든 요청에 접근 허용
public class HomeController {
    @GetMapping("/")
    public String hello(Model model) {
        return "home";
    }

}

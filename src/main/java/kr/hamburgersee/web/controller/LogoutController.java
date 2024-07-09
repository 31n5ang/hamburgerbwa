package kr.hamburgersee.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.web.WebConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            @RequestParam(defaultValue = "/", required = false) String redirect
    ) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:" + redirect;
    }
}

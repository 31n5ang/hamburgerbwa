package kr.hamburgersee.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.web.WebConst;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(WebConst.SESSION_LOGIN_MEMBER) == null) {
            // 미인증 상태라면
            response.sendRedirect("/login?redirect=" + request.getRequestURI());
            return false;
        } else {
            return true;
        }
    }
}

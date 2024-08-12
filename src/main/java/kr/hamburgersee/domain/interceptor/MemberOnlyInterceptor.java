package kr.hamburgersee.domain.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.annotation.RedirectStrategy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static kr.hamburgersee.domain.session.SessionAttrType.*;

public class MemberOnlyInterceptor implements HandlerInterceptor {
    public static final String LOGIN_PATH = "/login";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            // 컨트롤러가 핸들러 메소드인 경우에만 수행합니다.
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            MemberOnly methodAnnotation = handlerMethod.getMethodAnnotation(MemberOnly.class);
            if (methodAnnotation != null) {
                // MemberOnly 어노테이션이 존재한다면 회원 검증을 해야합니다.
                HttpSession session = request.getSession(true);
                if (session == null || session.getAttribute(MEMBER_SESSION_INFO.attribute) == null) {
                    // 리다이렉트 전략에 맞게 값을 할당합니다.
                    RedirectStrategy redirectStrategy = methodAnnotation.redirectStrategy();
                    String redirect = request.getRequestURI();
                    switch (redirectStrategy) {
                        case REQUEST -> {
                            redirect = request.getRequestURI();
                        }
                        case REFERER -> {
                            redirect = request.getHeader("Referer");
                        }
                    }
                    // 리다이렉트를 위한 요청 URI를 세션에 저장합니다.
                    session.setAttribute(REQUEST_REDIRECT_URI.attribute, redirect);
                    redirect(LOGIN_PATH, response);
                    return false;
                }
            }
        }

        // MemberOnly 어노테이션이 존재하지 않는다면 검증이 필요 없습니다.
        return true;
    }

    private void redirect(String uri, HttpServletResponse response) throws IOException {
        response.sendRedirect(uri);
    }
}

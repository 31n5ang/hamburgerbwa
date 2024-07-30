package kr.hamburgersee.domain.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.domain.annotation.LoginRedirectUri;
import kr.hamburgersee.domain.session.SessionConstants;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginRedirectUriArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginRedirectUriAnnotation = parameter.hasParameterAnnotation(LoginRedirectUri.class);
        boolean hasString = String.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginRedirectUriAnnotation && hasString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session != null) {
            return session.getAttribute(SessionConstants.REQUEST_REDIRECT_URI);
        }

        return null;
    }
}
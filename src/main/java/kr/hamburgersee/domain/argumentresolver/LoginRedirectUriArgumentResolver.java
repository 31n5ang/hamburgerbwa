package kr.hamburgersee.domain.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.domain.annotation.LoginRedirectUri;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static kr.hamburgersee.domain.session.SessionAttrType.REQUEST_REDIRECT_URI;

public class LoginRedirectUriArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @LoginRedirectUri String redirectUri 형태의 파라미터를 Resolve 합니다.
        boolean hasLoginRedirectUriAnnotation = parameter.hasParameterAnnotation(LoginRedirectUri.class);
        boolean hasString = String.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginRedirectUriAnnotation && hasString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session != null) {
            return session.getAttribute(REQUEST_REDIRECT_URI.attribute);
        }

        return "/";
    }
}

package kr.hamburgersee.domain.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.session.MemberSessionInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static kr.hamburgersee.domain.session.SessionAttrType.MEMBER_SESSION_INFO;

public class MemberSessionInfoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberOnlyAnnotation = parameter.hasMethodAnnotation(MemberOnly.class);
        boolean hasMemberSessionInfo = MemberSessionInfo.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberOnlyAnnotation && hasMemberSessionInfo;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session != null) {
            return session.getAttribute(MEMBER_SESSION_INFO.attribute);
        }

        return null;
    }
}

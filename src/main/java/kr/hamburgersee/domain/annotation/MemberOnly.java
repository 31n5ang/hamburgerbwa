package kr.hamburgersee.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberOnly {
    /**
     * RedirectStrategy 는 다음과 같습니다.
     * REQUEST : 로그인을 요청한 최근 URL로 리다이렉트 시키는 전략입니다.
     * REFERER : Referer 헤더 값을 리다이렉트 시킵니다. 쉽게 말해 요청 이전 URL로 리다이렉트 시킵니다.
     */
    RedirectStrategy redirectStrategy() default RedirectStrategy.REQUEST;
}

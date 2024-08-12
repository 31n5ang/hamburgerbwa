package kr.hamburgersee.config;

import kr.hamburgersee.domain.argumentresolver.LoginRedirectUriArgumentResolver;
import kr.hamburgersee.domain.argumentresolver.MemberSessionInfoArgumentResolver;
import kr.hamburgersee.domain.interceptor.MemberOnlyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 회원이 로그인되어있는 상태인지 검증합니다.
        registry.addInterceptor(new MemberOnlyInterceptor())
                .order(0)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberSessionInfoArgumentResolver());
        resolvers.add(new LoginRedirectUriArgumentResolver());
    }
}

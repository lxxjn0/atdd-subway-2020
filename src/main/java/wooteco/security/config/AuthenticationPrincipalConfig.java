package wooteco.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wooteco.security.web.AuthenticationPrincipalArgumentResolver;
import wooteco.security.web.OptionalAuthenticationPrincipalArgumentResolver;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
        argumentResolvers.add(createOptionalAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver();
    }

    @Bean
    public OptionalAuthenticationPrincipalArgumentResolver createOptionalAuthenticationPrincipalArgumentResolver() {
        return new OptionalAuthenticationPrincipalArgumentResolver();
    }
}

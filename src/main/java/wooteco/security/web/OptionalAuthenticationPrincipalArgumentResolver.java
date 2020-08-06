package wooteco.security.web;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import wooteco.security.core.Authentication;
import wooteco.security.core.OptionalAuthenticationPrincipal;
import wooteco.security.core.context.SecurityContextHolder;
import wooteco.subway.members.member.domain.LoginMember;

public class OptionalAuthenticationPrincipalArgumentResolver implements
        HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OptionalAuthenticationPrincipal.class);
    }

    @Override
    public Optional<Object> resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof Map) {
            return extractPrincipal(parameter, authentication);
        }

        return Optional.of(authentication.getPrincipal());
    }

    private Optional<Object> extractPrincipal(MethodParameter parameter,
            Authentication authentication) {
        try {
            Map<String, String> principal = (Map)authentication.getPrincipal();

            Object[] params = Arrays.stream(LoginMember.class.getDeclaredFields())
                    .map(it -> toObject(it.getType(), principal.get(it.getName())))
                    .toArray();

            return Optional.of(
                    LoginMember.class.getConstructors()[0].newInstance(params));
        } catch (Exception e) {
            throw new AuthorizationException();
        }
    }

    public static Object toObject(Class clazz, String value) {
        if (Boolean.class == clazz)
            return Boolean.parseBoolean(value);
        if (Byte.class == clazz)
            return Byte.parseByte(value);
        if (Short.class == clazz)
            return Short.parseShort(value);
        if (Integer.class == clazz)
            return Integer.parseInt(value);
        if (Long.class == clazz)
            return Long.parseLong(value);
        if (Float.class == clazz)
            return Float.parseFloat(value);
        if (Double.class == clazz)
            return Double.parseDouble(value);
        return value;
    }
}

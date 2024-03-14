package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // csrf 토큰 비활성화 -> 백엔드 개발 중에는. 프론트 연결 시에 고려
                .csrf(AbstractHttpConfigurer::disable)
                // form login 비활성화 ->
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        it -> it
                                .requestMatchers()
                        )
    }

}

package me.shinsunyoung.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // HTTP 보안 설정
        http.csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화

        // 요청 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/signup", "/user").permitAll() // 인증 없이 접근 허용
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
        );

        // 로그인 설정
        http.formLogin(form -> form
                .loginPage("/login") // 사용자 정의 로그인 페이지
                .defaultSuccessUrl("/articles") // 로그인 성공 시 이동할 URL
                .permitAll() // 로그인 페이지는 인증 없이 접근 가능
        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동할 URL
                .invalidateHttpSession(true) // 세션 무효화
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userService) // 사용자 정의 UserDetailsService 설정
                .passwordEncoder(bCryptPasswordEncoder); // 비밀번호 암호화 설정
        return authBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

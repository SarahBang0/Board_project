package board_project.board.config;

import board_project.board.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 비밀번호 암호화 도구 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API 테스트를 위해 CSRF는 잠시 끕니다.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/api/users/join","/users/join", "/login", "/", "/css/**", "/js/**").permitAll() // 회원가입은 누구나 가능
                        .requestMatchers("/users").hasRole("ADMIN")
                        .anyRequest().authenticated() // 나머지는 로그인해야 접근 가능
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") // ⭐ HTML의 input name과 일치시켜야 함
                        .passwordParameter("password")
                        .defaultSuccessUrl("/boards", true)
                        .permitAll()
                )
                // --- 여기부터 로그아웃 설정 추가 ---
                .logout(logout -> logout
                        .logoutUrl("/logout")            // 로그아웃을 처리할 URL (기본값도 /logout 이라 생략 가능)
                        .logoutSuccessUrl("/login")      // 로그아웃 성공 시 이동할 페이지
                        .invalidateHttpSession(true)     // 로그아웃 시 세션 삭제
                        .deleteCookies("JSESSIONID")     // 자동 로그인 쿠키 등 삭제
                )
                .exceptionHandling(conf -> conf
                        .accessDeniedPage("/error-403") // 에러 발생 시 이 경로(URL)로 가라!
                );

        return http.build();
    }
}

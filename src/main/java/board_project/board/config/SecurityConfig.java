package board_project.board.config;

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
                        .requestMatchers("/users/join", "/login", "/users", "/css/**", "/js/**").permitAll() // 회원가입은 누구나 가능
                        .anyRequest().authenticated() // 나머지는 로그인해야 접근 가능
                )
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지 경로 (나중에 뷰 만들 곳)
                        .defaultSuccessUrl("/boards") // 로그인 성공 시 이동할 곳
                        .permitAll()
                );

        return http.build();
    }
}

package tp.mediatogether.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tp.mediatogether.models.User;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/files/**")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("**")).permitAll()
                        .requestMatchers(toH2Console()).permitAll())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(toH2Console())
                        .ignoringRequestMatchers(new AntPathRequestMatcher("**"))) // dev only
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .logout((logout) -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    private final List<String> allowedOrigins;
//
//    public WebSecurityConfig(@Value("${allowed-origins}") String[] allowedOrigins) {
//        this.allowedOrigins = List.of(allowedOrigins);
//    }
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(allowedOrigins);
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfiguration.setAllowedHeaders(List.of("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }

}

package hr.riteh.rwt.ticketing.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
public class SecurityConfiguration {

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers(toH2Console()).permitAll()
                       .requestMatchers("/auth/**").permitAll()
                       .anyRequest().authenticated())
               .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
               //.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()))
               .csrf(AbstractHttpConfigurer::disable)
               .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
               .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
    }

}

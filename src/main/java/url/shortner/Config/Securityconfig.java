package url.shortner.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import url.shortner.Service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class Securityconfig {

    @Autowired
    MyUserDetailsService userDetailService;

    @Autowired
    JwtFilter jwtFilter;

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("register","loginuser","{shortUrl}")
                        .permitAll()

                        .anyRequest().authenticated())
                //.formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider =  new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
    }
}

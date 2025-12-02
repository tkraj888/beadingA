package com.spring.jwt.config;

import com.spring.jwt.config.filter.CustomAuthenticationProvider;
import com.spring.jwt.config.filter.JwtTokenAuthenticationFilter;
import com.spring.jwt.config.filter.JwtUsernamePasswordAuthenticationFilter;
import com.spring.jwt.exception.CustomAccessDeniedHandler;
import com.spring.jwt.jwt.JwtConfig;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.security.UserDetailsServiceCustom;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AppConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    @Lazy
    private JwtService jwtService;

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceCustom();
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
        return new JwtTokenAuthenticationFilter(jwtConfig, jwtService);
    }

    @Autowired
    public void configGlobal(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

        AuthenticationManager manager = builder.build();

        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests()
                .requestMatchers("/account/**").permitAll()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v*/a*-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                ).permitAll()
                .requestMatchers("/cars/**").permitAll()
                .requestMatchers("/biddingHTTP/**").permitAll()
                .requestMatchers("/finalBid/**").permitAll()
                .requestMatchers("/BeadingCarController/**").permitAll()
                .requestMatchers("/booking/**").permitAll()
                .requestMatchers("/userProfilePhoto/**").permitAll()
                .requestMatchers("/api/jwtUnAuthorize/**").permitAll()
                .requestMatchers("/uploadFile/**").permitAll()
                .requestMatchers("/confirmBooking/**").permitAll()
                .requestMatchers("/photo/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/dealerDocument/**").hasAnyAuthority("ADMIN", "DEALER")
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/dealer/allDealer").permitAll()
                .requestMatchers("/dealer/**").permitAll()
                .requestMatchers("/car/**").permitAll()
                .requestMatchers("/carVerify/**").permitAll()
                .requestMatchers("/sms/**").permitAll()
                .requestMatchers("/brands/**").permitAll()
                .requestMatchers("/Aucbidding/**").permitAll()
                .requestMatchers("/salesPerson/**").permitAll()
                .requestMatchers("/Bidding/v1/**").permitAll()
                .requestMatchers("/Bid/**").permitAll()
                .requestMatchers("/ispProfile/**").permitAll()
                .requestMatchers("/WalletAccount/**").permitAll()
                .requestMatchers("/transactions/**").permitAll()
                .requestMatchers("/uploadFileBidCar/**").permitAll()
                .requestMatchers("/inspectionReport/**").permitAll()
                .requestMatchers("/userFormController/**").permitAll()
                .requestMatchers("/saveCar/**").permitAll()
                .requestMatchers("/ProfilePhoto/**").permitAll()
                .requestMatchers("/colors/**").permitAll() 
                .requestMatchers("/b2b/**").permitAll()
                .requestMatchers("/b2bConfirm/**").permitAll()
                .requestMatchers("/premiumCars/**").permitAll()
                .requestMatchers("/PremiumCarUploadFile/**").permitAll()
                .requestMatchers("/enquiry/**").permitAll()
                .requestMatchers("/premiumBrands/**").permitAll()
                .requestMatchers("/PremiumCarFilter/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationManager(manager)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .addFilterBefore(new JwtUsernamePasswordAuthenticationFilter(manager, jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(Arrays.asList("https://caryanamindia.com","https://www.caryanamindia.com", "http://localhost:5173","https://localhost/**","https://localhost:3000","http://localhost:8081","https://caryanam.netlify.app/"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        };
    }
}

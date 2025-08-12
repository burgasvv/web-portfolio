package org.burgas.portfolioservice.config;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.entity.Authority;
import org.burgas.portfolioservice.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .authenticationManager(this.authenticationManager())
                .authorizeHttpRequests(
                        httpRequests -> httpRequests

                                .requestMatchers(
                                        "/api/v1/security/csrf-token",

                                        "/api/v1/identities/create",

                                        "/api/v1/professions", "/api/v1/professions/by-id",

                                        "/api/v1/portfolios", "/api/v1/portfolios/by-id",

                                        "/api/v1/projects/by-id",

                                        "/api/v1/images/by-id", "/api/v1/videos/by-id", "/api/v1/documents/by-id"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/v1/identities/by-id", "/api/v1/identities/update", "/api/v1/identities/delete",
                                        "/api/v1/identities/change-password", "/api/v1/identities/upload-image", "/api/v1/identities/change-image",
                                        "/api/v1/identities/delete-image",

                                        "/api/v1/portfolios/create", "/api/v1/portfolios/update", "/api/v1/portfolios/delete",

                                        "/api/v1/projects/create", "/api/v1/projects/update", "/api/v1/projects/delete",
                                        "/api/v1/projects/upload-images", "/api/v1/projects/change-image", "/api/v1/projects/delete-image",
                                        "/api/v1/projects/upload-videos", "/api/v1/projects/change-video", "/api/v1/projects/delete-video",
                                        "/api/v1/projects/upload-documents", "/api/v1/projects/change-document", "/api/v1/projects/delete-document"
                                )
                                .hasAnyAuthority(Authority.ADMIN.getAuthority(), Authority.USER.getAuthority())

                                .requestMatchers(
                                        "/api/v1/identities", "/api/v1/identities/enable-disable",

                                        "/api/v1/professions/create", "/api/v1/professions/update", "/api/v1/professions/delete"
                                )
                                .hasAnyAuthority(Authority.ADMIN.getAuthority())
                )
                .build();
    }
}

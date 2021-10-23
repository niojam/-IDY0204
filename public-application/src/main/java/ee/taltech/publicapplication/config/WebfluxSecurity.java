package ee.taltech.publicapplication.config;

import ee.taltech.publicapplication.game.security.converter.CookieAuthConverter;
import ee.taltech.publicapplication.game.security.converter.PublicAppAuthenticationConverter;
import ee.taltech.publicapplication.game.security.properties.SecurityProperties;
import ee.taltech.publicapplication.game.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import java.security.interfaces.RSAPublicKey;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
@EnableConfigurationProperties({SecurityProperties.class,})
public class WebfluxSecurity {

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .authorizeExchange()
                .pathMatchers(OPTIONS).permitAll()
                .pathMatchers(routesWithPermitAll()).permitAll()
                .pathMatchers("/player/access").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(cookieAuthFilter(), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder decoder() {
        RSAPublicKey publicKey = JwtUtils.publicKey(securityProperties.getPublicKey());
        return NimbusReactiveJwtDecoder
                .withPublicKey(publicKey)
                .signatureAlgorithm(SignatureAlgorithm.RS512)
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        JwtReactiveAuthenticationManager authenticationManager = new JwtReactiveAuthenticationManager(decoder());
        authenticationManager.setJwtAuthenticationConverter(
                new ReactiveJwtAuthenticationConverterAdapter(
                        new PublicAppAuthenticationConverter()
                )
        );
        return authenticationManager;
    }

    @Bean
    public AuthenticationWebFilter cookieAuthFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(new CookieAuthConverter(decoder()));
        return authenticationWebFilter;
    }

    private String[] routesWithPermitAll() {
        return new String[]{
                "/swagger-ui.html",
                "/webjars/swagger-ui/**",
                "/v3/api-docs/**"};
    }

}

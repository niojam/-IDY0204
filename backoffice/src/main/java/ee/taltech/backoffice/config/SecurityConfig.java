package ee.taltech.backoffice.config;


import ee.taltech.backoffice.game.service.AuthorService;
import ee.taltech.backoffice.game.service.QuizService;
import ee.taltech.backoffice.game.service.StatisticsService;
import ee.taltech.backoffice.security.filter.cookie.CookieAuthFilter;
import ee.taltech.backoffice.security.filter.cookie.CookieGeneratorService;
import ee.taltech.backoffice.security.filter.refresh.RefreshJwtFilter;
import ee.taltech.backoffice.security.filter.success_handler.JwtAuthorizationSuccessHandler;
import ee.taltech.backoffice.security.filter.success_handler.KahootOAuth2SuccessHandler;
import ee.taltech.backoffice.security.filter.success_handler.RefreshTokenSuccessHandler;
import ee.taltech.backoffice.security.properties.BackofficeSecurityProperties;
import ee.taltech.backoffice.security.service.BackofficeJwtGenerationService;
import ee.taltech.backoffice.security.service.JwtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BackofficeSecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BackofficeSecurityProperties properties;
    private final AuthorService authorService;
    private final QuizService quizService;
    private final StatisticsService statisticsService;
    private final BackofficeJwtGenerationService jwtGenerationService;
    private final CookieGeneratorService cookieGeneratorService;
    private final RefreshTokenSuccessHandler refreshTokenSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .formLogin().disable()
                .logout().disable()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(requestCookieAuthFilter(), OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(requestHeaderAuthFilter(), OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(refreshJwtFilter(refreshTokenSuccessHandler), OAuth2LoginAuthenticationFilter.class)
                .oauth2Login(auth -> auth.successHandler(
                        new KahootOAuth2SuccessHandler(authorService, jwtGenerationService, properties, cookieGeneratorService)));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/favicon.ico");
    }

    @Bean
    public CookieAuthFilter requestCookieAuthFilter() throws Exception {
        CookieAuthFilter filter = new CookieAuthFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(
                new JwtAuthorizationSuccessHandler(quizService, statisticsService, properties));
        FilterRegistrationBean<CookieAuthFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setEnabled(false);
        return bean.getFilter();
    }

    @Bean
    public RequestHeaderAuthenticationFilter requestHeaderAuthFilter() throws Exception {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setExceptionIfHeaderMissing(false);
        filter.setAuthenticationManager(authenticationManager());
        filter.setPrincipalRequestHeader(AUTHORIZATION);
        FilterRegistrationBean<RequestHeaderAuthenticationFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setEnabled(false);
        return bean.getFilter();
    }

    @Bean
    public RefreshJwtFilter refreshJwtFilter(RefreshTokenSuccessHandler refreshTokenSuccessHandler) throws Exception {
        RefreshJwtFilter filter = new RefreshJwtFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(refreshTokenSuccessHandler);
        FilterRegistrationBean<RefreshJwtFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setEnabled(false);
        return bean.getFilter();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(jwtAuthService());
        return provider;
    }

    @Bean
    public JwtAuthService jwtAuthService() {
        return new JwtAuthService(properties.getPublicKey());
    }

}

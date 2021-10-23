package ee.taltech.publicapplication.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;


@Configuration
@EnableRSocketSecurity
public class RSocketConfig {

    @Bean
    public PayloadSocketAcceptorInterceptor authorization(RSocketSecurity rsocketSecurity,
                                                          ReactiveAuthenticationManager authenticationManager) {
        return pattern(rsocketSecurity)
                .jwt(jwtSpec -> jwtSpec.authenticationManager(authenticationManager))
                .build();
    }

    @Bean
    public RSocketMessageHandler getMessageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        messageHandler.setRouteMatcher(new PathPatternRouteMatcher());
        messageHandler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        return messageHandler;
    }

    @Bean
    public NioEventLoopGroup nioEventLoopGroup() {
        return new NioEventLoopGroup(40);
    }

    private RSocketSecurity pattern(RSocketSecurity security) {
        return security.authorizePayload(authorize -> authorize
                .anyExchange().authenticated()
                .anyRequest().permitAll());
    }

}

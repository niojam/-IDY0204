package ee.taltech.publicapplication.game.security.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static ee.taltech.publicapplication.common.Constants.EXCHANGE_TOKEN_COOKIE;

@RequiredArgsConstructor
public class CookieAuthConverter implements ServerAuthenticationConverter {

    private final ReactiveJwtDecoder decoder;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.just(exchange)
                .map(ServerWebExchange::getRequest)
                .map(ServerHttpRequest::getCookies)
                .flatMap(cookies -> {
                    List<HttpCookie> exchangeTokenCookie = cookies.get(EXCHANGE_TOKEN_COOKIE);
                    if (exchangeTokenCookie != null && exchangeTokenCookie.size() == 1) {
                        return Mono.just(exchangeTokenCookie.get(0).getValue());
                    }
                    return Mono.empty();
                })
                .flatMap(decoder::decode)
                .map(jwt -> new BearerTokenAuthenticationToken(jwt.getTokenValue()));
    }

}

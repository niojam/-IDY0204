package ee.taltech.backoffice.security.filter.success_handler;

import ee.taltech.backoffice.game.model.Author;
import ee.taltech.backoffice.game.service.AuthorService;
import ee.taltech.backoffice.security.filter.cookie.CookieGeneratorService;
import ee.taltech.backoffice.security.properties.BackofficeSecurityProperties;
import ee.taltech.backoffice.security.service.BackofficeJwtGenerationService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ee.taltech.backoffice.constants.Constants.EMAIL;
import static ee.taltech.backoffice.constants.Constants.OID;
import static org.apache.http.HttpHeaders.LOCATION;

@RequiredArgsConstructor
public class KahootOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthorService authorService;
    private final BackofficeJwtGenerationService jwtGenerationService;
    private final BackofficeSecurityProperties properties;
    private final CookieGeneratorService cookieGeneratorService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        Author author = createIfNotExist(oAuth2AuthenticationToken);

        String accessToken = jwtGenerationService.generateAccessToken(author.getId());
        String refreshToken = jwtGenerationService.generateRefreshToken(author.getId());
        response.addCookie(cookieGeneratorService.composeAccessTokenCookie(accessToken));
        response.addCookie(cookieGeneratorService.composeRefreshTokenCookie(refreshToken));
        cookieGeneratorService.makeTokenCookiesSameSiteStrict(response);

        response.addHeader(LOCATION, properties.getFinalStepRedirectUri());
        response.setStatus(HttpStatus.SC_MOVED_TEMPORARILY);
    }

    private Author createIfNotExist(OAuth2AuthenticationToken token) {
        return authorService.createIfNotExist(
                token.getPrincipal().getAttribute(OID),
                token.getPrincipal().getAttribute(EMAIL));
    }

}

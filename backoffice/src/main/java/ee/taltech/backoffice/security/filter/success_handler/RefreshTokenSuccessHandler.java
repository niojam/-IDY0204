package ee.taltech.backoffice.security.filter.success_handler;

import ee.taltech.backoffice.security.filter.cookie.CookieGeneratorService;
import ee.taltech.backoffice.security.filter.user_details.BackofficeUserDetails;
import ee.taltech.backoffice.security.service.BackofficeJwtGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class RefreshTokenSuccessHandler implements AuthenticationSuccessHandler {

    private final BackofficeJwtGenerationService jwtGenerationService;
    private final CookieGeneratorService cookieGeneratorService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        BackofficeUserDetails userDetails = (BackofficeUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        String accessToken = jwtGenerationService.generateAccessToken(userId);
        Cookie accessTokenCookie = cookieGeneratorService.composeAccessTokenCookie(accessToken);
        response.addCookie(accessTokenCookie);
    }

}

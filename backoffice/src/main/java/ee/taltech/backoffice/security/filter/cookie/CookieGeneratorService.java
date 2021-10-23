package ee.taltech.backoffice.security.filter.cookie;

import ee.taltech.backoffice.security.properties.BackofficeSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static ee.taltech.backoffice.constants.Constants.ACCESS_TOKEN_COOKIE_NAME;
import static ee.taltech.backoffice.constants.Constants.REFRESH_TOKEN_COOKIE_NAME;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Service
@RequiredArgsConstructor
public class CookieGeneratorService {

    private final BackofficeSecurityProperties properties;
    private static final String SAME_SITE_FLAG = ";SameSite=Strict";

    public Cookie composeAccessTokenCookie(String jwt) {
        Cookie accessToken = new Cookie(ACCESS_TOKEN_COOKIE_NAME, jwt);
        // we should not allow js to access this jwt
        accessToken.setHttpOnly(true);
        accessToken.setPath("/");
        accessToken.setDomain(properties.getDomain());
        accessToken.setMaxAge(properties.getAccessTokenExpirationTime() * 3600);
        return accessToken;
    }

    public Cookie composeRefreshTokenCookie(String jwt) {
        Cookie refreshToken = new Cookie(REFRESH_TOKEN_COOKIE_NAME, jwt);
        // js should be able to access this one
        refreshToken.setHttpOnly(false);
        refreshToken.setPath("/");
        refreshToken.setDomain(properties.getDomain());
        refreshToken.setMaxAge(properties.getRefreshTokenExpirationTime() * 3600);
        return refreshToken;
    }

    public void makeTokenCookiesSameSiteStrict(HttpServletResponse response) {
        response.getHeaders(SET_COOKIE).stream()
                .filter(cookie -> cookie.startsWith(ACCESS_TOKEN_COOKIE_NAME) || cookie.startsWith(REFRESH_TOKEN_COOKIE_NAME))
                .forEach(cookie -> {
                    String sameSiteStrictCookie = cookie + SAME_SITE_FLAG;
                    response.addHeader(SET_COOKIE, sameSiteStrictCookie);
                });
    }

}

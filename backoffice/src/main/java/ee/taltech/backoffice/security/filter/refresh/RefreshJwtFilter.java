package ee.taltech.backoffice.security.filter.refresh;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

import static ee.taltech.backoffice.constants.Constants.REFRESH_TOKEN_COOKIE_NAME;

public class RefreshJwtFilter extends AbstractPreAuthenticatedProcessingFilter {

    public static final String PLACEHOLDER = "";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Optional<Cookie> refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                    .findFirst();
            if (refreshToken.isPresent()) {
                return refreshToken.get().getValue();
            }
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        // any value that is not equal to null
        return PLACEHOLDER;
    }

}

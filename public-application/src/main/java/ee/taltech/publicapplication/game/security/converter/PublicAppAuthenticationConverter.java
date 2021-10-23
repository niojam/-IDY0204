package ee.taltech.publicapplication.game.security.converter;

import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import ee.taltech.publicapplication.game.security.user_details.TokenExchangeUserDetails;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ee.taltech.publicapplication.common.Constants.*;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;

public class PublicAppAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Map<String, Object> claims = source.getClaims();

        Long userId = extractAndCheckId(claims);
        Long roomId = extractAndCheckRoomId(claims, source.getSubject());
        List<GrantedAuthority> grantedAuthorities = extractAndCheckGrantedAuthorities(claims);

        UserDetails userDetails = new PublicAppUserDetails(roomId, userId, grantedAuthorities);
        if (roomId == null) {
            userDetails = new TokenExchangeUserDetails(userId);
        }
        return new PreAuthenticatedAuthenticationToken(userDetails, null, grantedAuthorities);
    }

    private Long extractAndCheckId(Map<String, Object> claims) {
        assertNotNull("No userId provided", claims.get(USER_ID));
        return parseLong((String) claims.get(USER_ID));
    }

    private Long extractAndCheckRoomId(Map<String, Object> claims, String subject) {
        assertNotNull(subject);
        if (subject.equals(SUBJECT)) {
            assertNotNull("No roomId provided", claims.get(ROOM_ID));
            return parseLong((String) claims.get(ROOM_ID));
        }
        // on token exchange
        return null;
    }

    private List<GrantedAuthority> extractAndCheckGrantedAuthorities(Map<String, Object> claims) {
        assertNotNull("No authorities provided", claims.get(AUTHORITIES));
        return Arrays.stream(((String) claims.get(AUTHORITIES)).split(","))
                .map(r -> format("ROLE_%s", r))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}

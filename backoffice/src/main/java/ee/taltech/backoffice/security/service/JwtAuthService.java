package ee.taltech.backoffice.security.service;

import ee.taltech.backoffice.security.filter.user_details.BackofficeUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static ee.taltech.backoffice.constants.Constants.AUTHORITIES;
import static ee.taltech.backoffice.constants.Constants.USER_ID;
import static ee.taltech.backoffice.security.model.Authority.AUTHOR;
import static ee.taltech.backoffice.security.utils.RsaKeyUtils.publicKey;
import static java.lang.Long.parseLong;
import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final Resource publicKey;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        try {
            String jwt = (String) token.getPrincipal();

            if (jwt.startsWith("Bearer")) {
                jwt = jwt.substring(6);
            }

            String[] splitToken = jwt.split("\\.");
            Assert.isTrue(splitToken.length == 3, "JWT token must be split into 3 parts");

            // check signature
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey(publicKey))
                    .parseClaimsJws(jwt)
                    .getBody();

            // extract required claims
            Long authorId = checkAndExtractId(claims);
            List<GrantedAuthority> grantedAuthorities = checkAndExtractRoles(claims);

            // compose user details
            return new BackofficeUserDetails(authorId, grantedAuthorities);
        } catch (Exception e) {
            log.warn(format("invalid JWT: %s", e.getMessage()));
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    private Long checkAndExtractId(Claims claims) {
        assertClaim(claims.get(USER_ID));
        return parseLong((String) claims.get(USER_ID));
    }

    private List<GrantedAuthority> checkAndExtractRoles(Claims claims) throws AccessDeniedException {
        assertClaim(claims.get(AUTHORITIES));
        List<String> roles = List.of(((String) claims.get(AUTHORITIES)).split(","));
        if (!roles.contains(AUTHOR.name())) {
            throw new AccessDeniedException("Not enough rights");
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void assertClaim(Object value) {
        Assert.hasText((String) value, "JWT is missing required claim");
    }

}

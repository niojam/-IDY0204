package ee.taltech.backoffice.security.service;

import ee.taltech.backoffice.security.properties.BackofficeSecurityProperties;
import ee.taltech.backoffice.security.utils.RsaKeyUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ee.taltech.backoffice.constants.Constants.*;

@Component
@RequiredArgsConstructor
public class BackofficeJwtGenerationService {

    private final BackofficeSecurityProperties properties;
    private Long accessTokenExpirationTime;
    private Long refreshTokenExpirationTime;

    @PostConstruct
    void setAccessTokenExpirationTime() {
        accessTokenExpirationTime = TimeUnit.HOURS.toMillis(properties.getAccessTokenExpirationTime());
        refreshTokenExpirationTime = TimeUnit.HOURS.toMillis(properties.getRefreshTokenExpirationTime());
    }

    public String generateAccessToken(Long id) {
        return Jwts.builder()
                .setClaims(composeAccessTokenClaims(id))
                .setSubject(SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.RS512, RsaKeyUtils.privateKey(properties.getPrivateKey()))
                .compact();
    }

    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .setClaims(composeAccessTokenClaims(id))
                .setSubject(SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(SignatureAlgorithm.RS512, RsaKeyUtils.privateKey(properties.getPrivateKey()))
                .compact();
    }

    private Map<String, Object> composeAccessTokenClaims(Long authorId) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(AUTHORITIES, AUTHOR);
        claimsMap.put(USER_ID, authorId.toString());
        return new DefaultClaims(claimsMap);
    }

}

package ee.taltech.publicapplication.game.security.service;

import ee.taltech.publicapplication.game.security.model.Authority;
import ee.taltech.publicapplication.game.security.properties.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static ee.taltech.publicapplication.common.Constants.*;
import static ee.taltech.publicapplication.game.security.utils.JwtUtils.concatAuthorities;
import static ee.taltech.publicapplication.game.security.utils.JwtUtils.privateKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(1);
    private final SecurityProperties securityProperties;

    public String generateJwt(Long roomId,
                              Long userId,
                              List<Authority> grantedAuthorities) {
        return Jwts.builder()
                .setClaims(generateClaims(roomId, userId, grantedAuthorities))
                .setSubject(SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(
                        SignatureAlgorithm.RS512,
                        privateKey(securityProperties.getPrivateKey()))
                .compact();
    }

    private Map<String, Object> generateClaims(Long roomId,
                                               Long userId,
                                               List<Authority> grantedAuthorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES, concatAuthorities(grantedAuthorities));
        claims.put(USER_ID, String.valueOf(userId));
        claims.put(ROOM_ID, String.valueOf(roomId));
        return claims;
    }

}

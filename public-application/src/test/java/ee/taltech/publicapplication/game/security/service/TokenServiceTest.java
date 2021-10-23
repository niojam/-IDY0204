package ee.taltech.publicapplication.game.security.service;

import ee.taltech.publicapplication.game.abstract_test.AbstractTestWithRepository;
import ee.taltech.publicapplication.game.security.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

import static ee.taltech.publicapplication.common.Constants.*;
import static ee.taltech.publicapplication.game.security.model.Authority.PLAYER;
import static ee.taltech.publicapplication.game.security.utils.JwtUtils.publicKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TokenServiceTest extends AbstractTestWithRepository {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Description("Should successfully generate jwt")
    void shouldGenerateJwt() {
        String jwt = tokenService.generateJwt(-2L, -15L, List.of(PLAYER));
        Jws<Claims> jwtClaims = parseAndValidate(jwt, securityProperties.getPublicKey());
        assertThat(jwtClaims.getBody().get(USER_ID)).isEqualTo("-15");
        assertThat(jwtClaims.getBody().get(ROOM_ID)).isEqualTo("-2");
        assertThat(jwtClaims.getBody().get(AUTHORITIES)).isEqualTo("PLAYER");
        assertThat(jwtClaims.getBody().getSubject()).isEqualTo("PUBLIC_APP");
    }

    @Test
    @Description("Should not validate wrong jwt")
    void shouldNotValidateWrongJwt() {
        String jwt = tokenService.generateJwt(-2L, -1L, List.of(PLAYER));
        String wrongJwt = jwt.substring(0, jwt.length() - 5) + "abcde";
        assertThatThrownBy(
                () -> parseAndValidate(wrongJwt, securityProperties.getPublicKey())
        )
                .isInstanceOf(SignatureException.class)
                .hasMessage("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");

    }

    private Jws<Claims> parseAndValidate(String jwt, Resource publicKey) {
        return Jwts.parser()
                .setSigningKey(publicKey(publicKey))
                .parseClaimsJws(jwt);
    }

}
package ee.taltech.publicapplication.game.utils;

import ee.taltech.publicapplication.game.security.user_details.PublicAppUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static ee.taltech.publicapplication.game.security.model.Authority.AUTHOR;
import static ee.taltech.publicapplication.game.security.model.Authority.PLAYER;

@UtilityClass
public class TestUtils {

    public static final String PLAYER_TOKEN_WITHOUT_BEARER = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiJQVUJMSUNfQVBQIiwidXNlcklkIjoiNyIsInJvb21JZCI6Ii0xIiwiYXV0aG9yaXRpZXMiOiJQTEFZRVIifQ.JeuAapmPMmomsrawpm7JA7pA9u7Sydea3zh_vl5NK1DRMoANiZqhAuEsYvEES71kuhSJ3NXYBY41aMkkfYAm_gDKlf91WStpwVdHUNR9DLnq2tgfaItnruIuInuzVFsjjf8aim1MzLiOXf7z0UpBhh6ngmyMsJSzSo8OiAzzOvj2PEL-cxWhPC3CpmmgqPpmMdidGz9d0aapr9XwjOIkHZJ5PeLrc3rnSr2jXLbWXAvvqrqro_WhUTYiRsUG6vJmzObqrW-KOK2_4E2taXjbmyMFNChusqLHqEF9lSXHNjko7bbFpxJ7GRJ1s-d7ZgT2F77ZpLsh27II2eFCGkDf8g";
    public static final String PLAYER_TOKEN = "Bearer " + PLAYER_TOKEN_WITHOUT_BEARER;

    public static PublicAppUserDetails playerPublicAppUserDetails(long playerId) {
        return new PublicAppUserDetails(
                playerId,
                -1L,
                List.of(new SimpleGrantedAuthority("ROLE_" + PLAYER.name())));
    }

    public static PublicAppUserDetails playerPublicAppUserDetails() {
        return new PublicAppUserDetails(
                7L,
                -1L,
                List.of(new SimpleGrantedAuthority("ROLE_" + PLAYER.name())));
    }

    public static PublicAppUserDetails authorPublicAppUserDetails() {
        return new PublicAppUserDetails(
                -1L,
                -1L,
                List.of(new SimpleGrantedAuthority("ROLE_" + AUTHOR.name())));
    }

}

package ee.taltech.backoffice.security.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Validated
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("security")
public class BackofficeSecurityProperties {

    @NotNull
    private final Resource privateKey;

    @NotNull
    private final Resource publicKey;

    @NotBlank
    private final String finalStepRedirectUri;

    @NotNull
    private final Integer accessTokenExpirationTime;

    @NotNull
    private final Integer refreshTokenExpirationTime;

    @NotEmpty
    private final List<String> quizRelatedUris;

    @NotEmpty
    private final List<String> ignoredQuizRelatedUris;

    @NotEmpty
    private final List<String> statisticsRelatedUris;

    @NotEmpty
    private final List<String> roomRelatedUris;

    @NotEmpty
    private final List<String> ignoredRoomRelatedUris;

    @NotEmpty
    private final String domain;

}

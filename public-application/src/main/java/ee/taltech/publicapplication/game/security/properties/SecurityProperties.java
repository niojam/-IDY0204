package ee.taltech.publicapplication.game.security.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Validated
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("kahoot.security")
public class SecurityProperties {

    @NotNull
    private final Resource publicKey;

    @NotNull
    private final Resource privateKey;

}

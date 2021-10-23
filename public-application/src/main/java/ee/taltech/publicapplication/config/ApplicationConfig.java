package ee.taltech.publicapplication.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Clock;
import java.time.ZoneId;

@Configuration
@EnableConfigurationProperties({ApplicationConfig.GameProperties.class})
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Helsinki"));
    }

    @Getter
    @Validated
    @ConstructorBinding
    @RequiredArgsConstructor
    @ConfigurationProperties("game")
    public static class GameProperties {

        @NotBlank
        private final String roomRegisteredRerouteLocation;

        @NotBlank
        private final String domain;

    }

}

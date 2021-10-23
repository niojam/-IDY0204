package ee.taltech.backoffice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenAPIConfiguration.OpenAPIConfigurationProperties.class)
public class OpenAPIConfiguration {

    private final OpenAPIConfigurationProperties properties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(properties.getPrefix()))
                .components(new Components())
                .security(List.of(new SecurityRequirement().addList("jwt")))
                .components(new Components().securitySchemes(Map.of(
                        "jwt",
                        new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                .name(AUTHORIZATION)
                                .in(SecurityScheme.In.HEADER)
                )))
                .info(new Info()
                        .version("1")
                        .title("TalTech quiz Back Office")
                        .description("This service provides back office functionality for TalTech quiz infrastructure\n\n\n Token: Bearer eyJhbGciOiJSUzUxMiJ9.eyJ1c2VySWQiOiItMSIsImF1dGhvcml0aWVzIjoiQVVUSE9SIn0.csdX9oek0u0bAyOT_kkjiQZ_9qT7PRd7bElqKSEF0AH_mOmwXOxVvuWFQXYJkmwntd9xZIs0dlBHB5KHeK2k9X2Lc7m9Jx8vBjyLbP8VYj6f4znM9KUr8E4lox4D_BpKRkhVjS45W8C5Yj0E4K8qFHb7g0Wkb7fhySyAspUFzAe-o80xNu982AiGBuVOvYiXfSpt0T4UNuo7bKFQ9dLm09imEfQdqyq0Z8NSWGa1A7NFEBINLa-F0DXoHnLH8h44ReI8Yy8J4P626BHgL5HM7tKM9KSPwJiPuBvvU1BU2IMzfrhBuMlVwPXRNQHjewoc1AVSdpKVu1sm8N-bzuxCPA"));
    }

    @Getter
    @Validated
    @ConstructorBinding
    @RequiredArgsConstructor
    @ConfigurationProperties("kahoot.openapi")
    public static class OpenAPIConfigurationProperties {

        @NotBlank
        private final String prefix;

    }

}
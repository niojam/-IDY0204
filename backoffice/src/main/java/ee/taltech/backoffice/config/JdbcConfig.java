package ee.taltech.backoffice.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JdbcConfig.KahootJdbcProperties.class})
@EnableJdbcRepositories(basePackages = "ee.taltech.backoffice")
public class JdbcConfig extends AbstractJdbcConfiguration {

    private final KahootJdbcProperties properties;

    @Bean
    public NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            @Override
            @SuppressWarnings("NullableProblems")
            public String getSchema() {
                return properties.getSchema();
            }
        };
    }

    @Bean
    @Override
    @SuppressWarnings("NullableProblems")
    public JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy, JdbcCustomConversions customConversions) {
        JdbcMappingContext mappingContext = super.jdbcMappingContext(namingStrategy, customConversions);
        mappingContext.setForceQuote(false);
        return mappingContext;
    }

    @Getter
    @Validated
    @ConstructorBinding
    @RequiredArgsConstructor
    @ConfigurationProperties("kahoot.db")
    public static class KahootJdbcProperties {

        @NotBlank
        private final String schema;
    }
}

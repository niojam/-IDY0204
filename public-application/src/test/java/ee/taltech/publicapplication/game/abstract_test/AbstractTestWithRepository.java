package ee.taltech.publicapplication.game.abstract_test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment;

@SpringBootTest
@Testcontainers
@DirtiesContext // TODO find out the reason
@ActiveProfiles({"integration-test",})
@ContextConfiguration(initializers = AbstractTestWithRepository.TestEnvInitializer.class)
public abstract class AbstractTestWithRepository {

    // TODO add image
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>();

    public static class TestEnvInitializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        @SuppressWarnings("NullableProblems")
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            addInlinedPropertiesToEnvironment(configurableApplicationContext, String.format("spring.liquibase.url=jdbc:postgresql://localhost:%d/%s", postgreSQLContainer.getFirstMappedPort(), postgreSQLContainer.getDatabaseName()));
            addInlinedPropertiesToEnvironment(configurableApplicationContext, "spring.liquibase.user=" + postgreSQLContainer.getUsername());
            addInlinedPropertiesToEnvironment(configurableApplicationContext, "spring.liquibase.password=" + postgreSQLContainer.getPassword());
            addInlinedPropertiesToEnvironment(configurableApplicationContext, "spring.liquibase.changeLog=classpath:db/changelog/master.xml");


            addInlinedPropertiesToEnvironment(configurableApplicationContext, String.format("spring.r2dbc.url=r2dbc:postgresql://localhost:%d/%s", postgreSQLContainer.getFirstMappedPort(), postgreSQLContainer.getDatabaseName()));
            addInlinedPropertiesToEnvironment(configurableApplicationContext, "spring.r2dbc.username=" + postgreSQLContainer.getUsername());
            addInlinedPropertiesToEnvironment(configurableApplicationContext, "spring.r2dbc.password=" + postgreSQLContainer.getPassword());
        }
    }

}

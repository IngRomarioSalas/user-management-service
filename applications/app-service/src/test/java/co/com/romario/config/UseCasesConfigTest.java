package co.com.romario.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import co.com.romario.model.user.gateways.UserRepository;
import co.com.romario.usecase.createuser.CreateUserUseCase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {

            // Aseguramos que efectivamente existe el bean
            CreateUserUseCase useCase = context.getBean(CreateUserUseCase.class);
            assertNotNull(useCase, "CreateUserUseCase bean should not be null");

            // Verificamos que tiene la dependencia inyectada correctamente
            UserRepository repo = context.getBean(UserRepository.class);
            assertSame(repo, extractUserRepository(useCase),
                    "The UserRepository injected into CreateUserUseCase is not the expected mock");
        }
    }

    private UserRepository extractUserRepository(CreateUserUseCase useCase) {
        return (UserRepository) ReflectionTestUtils.getField(useCase, "userRepository");
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }
}
package co.com.romario.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import co.com.romario.model.user.User;
import co.com.romario.r2dbc.entity.UserEntity;
import co.com.romario.r2dbc.mapper.UserMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    private MyReactiveRepository repository;
    private UserMapper mapper; // antes era ObjectMapper, ahora es UserMapper
    private ObjectMapper objectMapper; // necesario para el constructor
    private MyReactiveRepositoryAdapter adapter;


    @BeforeEach
    void setUp() {
        repository = Mockito.mock(MyReactiveRepository.class);
        mapper = new UserMapper(); // usamos la implementación real de UserMapper
        objectMapper = Mockito.mock(ObjectMapper.class); // puedes mockearlo si no se usa en tests

        adapter = new MyReactiveRepositoryAdapter(repository, mapper, objectMapper);
    }

    @Test
    void existsByEmail_true() {
        String email = "test@example.com";
        Mockito.lenient().when(repository.existsByEmail(email)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByEmail(email))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByEmail_false() {
        String email = "noexist@example.com";
        when(repository.existsByEmail(email)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsByEmail(email))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void save_user_mappedCorrectly() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .names("Juan")
                .lastName("Pérez")
                .email("juan@example.com")
                .baseSalary(2_500_000.0)
                .build();

        // Simula repository.save para devolver siempre un Mono
        Mockito.lenient()
                .when(repository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(adapter.save(user))
                .expectNextMatches(u -> u.getEmail().equals("juan@example.com") && u.getNames().equals("Juan"))
                .verifyComplete();
    }
    
}
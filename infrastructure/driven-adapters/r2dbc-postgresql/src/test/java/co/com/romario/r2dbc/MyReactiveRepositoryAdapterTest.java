package co.com.romario.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivecommons.utils.ObjectMapper;

import co.com.romario.model.user.User;
import co.com.romario.r2dbc.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MyReactiveRepositoryAdapterTest {

    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new MyReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void mustFindValueById() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        User user = new User();

        lenient().when(repository.findById(1L)).thenReturn(Mono.just(entity));
        lenient().when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity entity = new UserEntity();
        User user = new User();

        lenient().when(repository.findAll()).thenReturn(Flux.just(entity));
        lenient().when(mapper.map(entity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    // @Test
    // void mustFindByExample() {
    //     UserEntity entity = new UserEntity();
    //     User user = new User();

    //     lenient().when(repository.findAll(any())).thenReturn(Flux.just(entity));
    //     lenient().when(mapper.map(entity, User.class)).thenReturn(user);

    //     Flux<User> result = repositoryAdapter.findByExample(user);

    //     StepVerifier.create(result)
    //             .expectNext(user)
    //             .verifyComplete();
    // }

    @Test
void mustSaveValue() {
    UserEntity entity = new UserEntity();
    User user = new User();

    when(mapper.map(user, UserEntity.class)).thenReturn(entity); // <- conversión E -> D
    when(repository.save(entity)).thenReturn(Mono.just(entity));
    when(mapper.map(entity, User.class)).thenReturn(user); // <- conversión D -> E

    Mono<User> result = repositoryAdapter.save(user);

    StepVerifier.create(result)
            .expectNext(user)
            .verifyComplete();
}

    @Test
    void mustFindByEmail() {
        UserEntity entity = new UserEntity();
        User user = new User();

        lenient().when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(entity));
        lenient().when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findByEmail("test@email.com");

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustExistByEmail() {
        UserEntity entity = new UserEntity();

        lenient().when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(entity));

        Mono<Boolean> result = repositoryAdapter.existsByEmail("test@email.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

}

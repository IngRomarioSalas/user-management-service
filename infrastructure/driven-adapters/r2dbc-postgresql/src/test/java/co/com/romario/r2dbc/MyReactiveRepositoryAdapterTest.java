package co.com.romario.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import co.com.romario.model.user.User;
import co.com.romario.r2dbc.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    // -----------------------------
    // findById
    // -----------------------------
    @Test
    void mustFindValueById() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        User user = new User();
        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        StepVerifier.create(repositoryAdapter.findById(1L))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindValueById_empty() {
        when(repository.findById(2L)).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findById(2L))
                .verifyComplete();
    }

    // -----------------------------
    // findAll
    // -----------------------------
    @Test
    void mustFindAllValues() {
        UserEntity entity = new UserEntity();
        User user = new User();
        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        StepVerifier.create(repositoryAdapter.findAll())
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues_empty() {
        when(repository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(repositoryAdapter.findAll())
                .verifyComplete();
    }

    // -----------------------------
    // findByExample
    // -----------------------------

// Para findByExample con resultado
// @Test
// void mustFindByExample() {
//     UserEntity entity = new UserEntity();
//     User user = new User();

//     when(repository.findAll(argThat(example ->
//             example != null && example.getProbe() != null
//     ))).thenReturn(Flux.just(entity));

//     when(mapper.map(entity, User.class)).thenReturn(user);

//     Flux<User> result = repositoryAdapter.findByExample(user);

//     StepVerifier.create(result)
//             .expectNext(user)
//             .verifyComplete();
// }

// Para findByExample vacío
// @Test
// void mustFindByExample_empty() {
//     User user = new User();

//     when(repository.findAll(argThat(example ->
//             example != null && example.getProbe() != null
//     ))).thenReturn(Flux.empty());

//     Flux<User> result = repositoryAdapter.findByExample(user);

//     StepVerifier.create(result)
//             .verifyComplete();
// }


    // -----------------------------
    // save
    // -----------------------------
    @Test
    void mustSaveValue() {
        UserEntity entity = new UserEntity();
        User user = new User();

        // Mappeo desde entidad a dominio
        when(mapper.map(any(User.class), eq(UserEntity.class))).thenReturn(entity);
        when(mapper.map(any(UserEntity.class), eq(User.class))).thenReturn(user);

        // Stub del repository
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(entity));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    // -----------------------------
    // findByEmail
    // -----------------------------
    @Test
    void mustFindByEmail() {
        UserEntity entity = new UserEntity();
        User user = new User();
        when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        StepVerifier.create(repositoryAdapter.findByEmail("test@email.com"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindByEmail_empty() {
        when(repository.findByEmail("missing@email.com")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByEmail("missing@email.com"))
                .verifyComplete();
    }

    // -----------------------------
    // existsByEmail
    // -----------------------------
    @Test
    void mustExistByEmail_true() {
        UserEntity entity = new UserEntity();
        when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(entity));

        StepVerifier.create(repositoryAdapter.existsByEmail("test@email.com"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustExistByEmail_false() {
        when(repository.findByEmail("missing@email.com")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.existsByEmail("missing@email.com"))
                .expectNext(false)
                .verifyComplete();
    }

    // -----------------------------
    // findByDocumentNumber / existsByDocumentNumber
    // -----------------------------
    @Test
    void mustFindByDocumentNumber() {
        UserEntity entity = new UserEntity();
        User user = new User();
        when(repository.findByDocumentNumber("123")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        StepVerifier.create(repositoryAdapter.findByDocumentNumber("123"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFindByDocumentNumber_empty() {
        when(repository.findByDocumentNumber("999")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByDocumentNumber("999"))
                .verifyComplete();
    }

    @Test
    void mustExistByDocumentNumber_true() {
        UserEntity entity = new UserEntity();
        when(repository.findByDocumentNumber("123")).thenReturn(Mono.just(entity));

        StepVerifier.create(repositoryAdapter.existsByDocumentNumber("123"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustExistByDocumentNumber_false() {
        when(repository.findByDocumentNumber("999")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.existsByDocumentNumber("999"))
                .expectNext(false)
                .verifyComplete();
    }
}

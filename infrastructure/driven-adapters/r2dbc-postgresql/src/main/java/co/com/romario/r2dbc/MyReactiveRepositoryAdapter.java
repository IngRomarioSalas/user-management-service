package co.com.romario.r2dbc;

import co.com.romario.model.user.User;
import co.com.romario.model.user.gateways.UserRepository;
import co.com.romario.r2dbc.entity.UserEntity;
import co.com.romario.r2dbc.helper.ReactiveAdapterOperations;
import co.com.romario.r2dbc.mapper.UserMapper;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User/* change for domain model */,
    UserEntity/* change for adapter model */,
    UUID,
    MyReactiveRepository
> implements UserRepository {

    private final MyReactiveRepository repository;
    private final UserMapper mapper;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, UserMapper mapper, ObjectMapper objectMapper) {
        // llama al constructor de ReactiveAdapterOperations con los tres parámetros
        super(repository, objectMapper, entity -> mapper.toModel(entity));

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    /* @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.toEntity(user);
        return repository.save(entity)
                .flatMap(e -> {
                    User model = mapper.toModel(e);
                    return model != null ? Mono.just(model) : Mono.empty();
                });
    } */

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.toEntity(user);
        return repository.save(entity)
                .map(e -> {
                    User model = mapper.toModel(e);
                    if (model == null)
                        throw new IllegalStateException("Mapper returned null");
                    return model;
                });
    }

}

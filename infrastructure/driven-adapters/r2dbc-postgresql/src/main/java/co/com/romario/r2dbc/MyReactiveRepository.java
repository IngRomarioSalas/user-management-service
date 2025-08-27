package co.com.romario.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.romario.r2dbc.entity.UserEntity;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    @Query("SELECT * FROM users WHERE user_id = :userId")
    Mono<UserEntity> findByUserId(String userId);

    @Query("SELECT * FROM users WHERE correo_electronico = :email")
    Mono<UserEntity> findByEmail(String email);

    @Query("SELECT * FROM users WHERE numero_documento = :documentNumber")
    Mono<UserEntity> findByDocumentNumber(String documentNumber);

}

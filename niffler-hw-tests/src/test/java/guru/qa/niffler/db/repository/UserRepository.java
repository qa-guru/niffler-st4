package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);

    UserEntity createInUserdata(UserEntity user);

    Optional<UserAuthEntity> findByIdInAuth(UUID id);

    Optional<UserEntity> findByIdInUserdata(UUID id);
    UserAuthEntity findByUsernameInAuth(String username);

    UserEntity findByUsernameInUserdata(String username);

    UserAuthEntity updateInAuth(UserAuthEntity user);

    UserEntity updateInUserdata(UserEntity user);

    void deleteInAuthById(UUID id);

    void deleteInUserdataById(UUID id);
}

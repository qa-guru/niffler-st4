package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  UserAuthEntity createInAuth(UserAuthEntity user);

  Optional<UserAuthEntity> findByIdInAuth(UUID id);

  UserEntity createInUserdata(UserEntity user);

  Optional<UserEntity> findByIdInUserdata(UUID id);

  void deleteInAuthById(UUID id);

  void deleteInUserdataById(UUID id);
}

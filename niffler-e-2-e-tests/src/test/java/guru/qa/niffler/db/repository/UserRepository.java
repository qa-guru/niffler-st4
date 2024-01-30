package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  UserAuthEntity createInAuth(UserAuthEntity user);

  UserEntity createInUserdata(UserEntity user);

  void deleteInAuthById(UUID id);

  void deleteInUserdataById(UUID id);

  Optional<UserAuthEntity> findUserInAuthById(UUID id);

  Optional<UserEntity> findUserInUserdataById(UUID id);

  void updateUserInAuth(UserAuthEntity userAuthEntity);

  void updateUserInUserdata(UserEntity userEntity);

  List<AuthorityEntity> findAuthoritiesByUserId(UUID userId);

}

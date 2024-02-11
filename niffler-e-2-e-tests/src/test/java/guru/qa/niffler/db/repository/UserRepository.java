package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);

    UserEntity createInUserdata(UserEntity user);

    void deleteInAuthById(UUID id);

    void deleteInUserdataById(UUID id);

    void updateCurrencyByUsername(String userName, CurrencyValues currency);

    UserEntity getUserDataByName(String name);

    Optional<UserEntity> findByIdInUserdata(UUID id);

    Optional<UserAuthEntity> findByIdInAuth(UUID id);

    UserEntity updateInUserdata(UserEntity user);

    UserAuthEntity updateInAuth(UserAuthEntity user);
}

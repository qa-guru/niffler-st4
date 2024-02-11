package guru.qa.niffler.test;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.UserDb;
import guru.qa.niffler.jupiter.UserRepositoryExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserRepositoryExtension.class)
public class LoginTest extends BaseWebTest {

    private UserRepository userRepository;

    @Test
    @UserDb(username = "Ivan", password = "12345", deleteAfterTest = true)
    void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
        welcomePage.open().clickLoginAndGoToLoginPage().loginInUser(
                userAuth.getUsername(),
                userAuth.getPassword()
        ).statisticsSectionShouldExist();
    }

    @Test
    @UserDb()
    void shouldChangeDefaultCurrency(UserAuthEntity userAuth) {
        welcomePage.open().clickLoginAndGoToLoginPage().loginInUser(
                userAuth.getUsername(),
                userAuth.getPassword()
        ).goToProfile().verifyDefaultCurrencyValue(CurrencyValues.RUB);
        userRepository.updateCurrencyByUsername(userAuth.getUsername(), CurrencyValues.USD);
        profilePage.refreshProfilePage().verifyDefaultCurrencyValue(CurrencyValues.USD);
    }

    @Test
    @UserDb(username = "Vasya", password = "12345")
    public void shouldSetFirstAndSurName(UserAuthEntity userAuth) {
        welcomePage.open().clickLoginAndGoToLoginPage().loginInUser(
                userAuth.getUsername(),
                userAuth.getPassword()
        ).goToProfile();

        String firstName = faker.name().firstName();
        String surName = faker.name().lastName();

        profilePage.setFirstAndSurName(firstName, surName);

        UserEntity user = userRepository.getUserDataByName(userAuth.getUsername());
        Assertions.assertEquals(firstName, user.getFirstname());
        Assertions.assertEquals(surName, user.getSurname());
    }
}

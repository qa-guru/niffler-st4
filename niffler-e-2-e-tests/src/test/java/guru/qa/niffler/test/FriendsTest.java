package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;


@ExtendWith(UsersQueueExtension.class)
public class FriendsTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(WITH_FRIENDS) UserJson user) {
        welcomePage.open().clickLoginAndGoToLoginPage().loginInUser(user.username(), user.testData().password());
    }

    @Test
    @DisplayName("У пользователя есть один друг в списке друзей")
    void friendsTableShouldNotBeEmpty(@User(WITH_FRIENDS) UserJson user) {
        mainPage.goToFriendsPage().verifyThatFriendsListNotEmpty()
                .goToProfile().verifyProfileName(user.username());
    }

    @Test
    @DisplayName("Проверка наличия кнопки 'Remove Friend' в списке друзей")
    void friendShouldHaveRemoveButton(@User(WITH_FRIENDS) UserJson user) {
        mainPage.goToFriendsPage().verifyExistingButtonRemoveFriend()
                .goToProfile().verifyProfileName(user.username());

    }

    @Test
    @DisplayName("Проверка навигации по основным разделам приложения")
    void navigationTest() {
        mainPage.goToFriendsPage().goToPeoplePage().goToProfile()
                .goToPeoplePage().goToFriendsPage().goToMainPage();
    }
}

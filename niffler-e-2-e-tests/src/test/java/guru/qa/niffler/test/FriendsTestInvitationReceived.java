package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.INVITATION_RECEIVED;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTestInvitationReceived extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        welcomePage.open().clickLoginAndGoToLoginPage();
    }

    @Test
    @DisplayName("Проверка получения приглашения дружбы")
    void userShouldSeeFriendsInvitation(@User(INVITATION_RECEIVED) UserJson user) {
        loginPage.loginInUser(user.username(), user.testData().password())
                .goToFriendsPage().verifyReceivedFriendsInvitation();
    }

    @DisplayName("Проверим, что есть кнопка отклонить запрос в друзья")
    @Test
    void invitationTableShouldHaveDeclineInvitationBtn(@User(INVITATION_RECEIVED) UserJson user) {
        loginPage.loginInUser(user.username(), user.testData().password())
                .goToPeoplePage().verifyExistingDeclineInvitationBtn();
    }
}

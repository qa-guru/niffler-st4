package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

@ExtendWith(UsersQueueExtension.class)
public class InvitationSentFriendsTest extends BaseWebTest {

    @BeforeEach()
    void openFriendsPage(@User(INVITATION_SENT) UserJson user) {
        welcomePage
                .open()
                .clickLoginButton()
                .authorize(user.username(), user.testData().password()).clickFriendsButton();
    }

    @Test
    @DisplayName("Проверка записей в таблице друзей")
    void checkTableFriendIsEmpty() {
        friendsPage
                .checkTableFriendIsEmpty();
    }

    @Test
    @DisplayName("Проверка отображения кнопки Submit invitation")
    void submitInvitationVisible() {
        friendsPage
                .checkSubmitInvitationVisible();
    }

    @Test
    @DisplayName("Проверка отображения кнопки Decline invitation")
    void declineInvitationVisible() {
        friendsPage
                .checkDeclineInvitationVisible();
    }
}

package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.INVITATION_SENT;

@ExtendWith(UsersQueueExtension.class)
public class InvitationSentFriendsTest {

    @BeforeEach()
    void openFriendsPage(@User(INVITATION_SENT) UserJson user) {
        new WelcomePage().open().clickLoginButton()
                .authorize(user.username(), user.testData().password()).clickFriendsButton();
    }

    @Test
    @DisplayName("Проверка записей в таблице друзей")
    void checkTableFriendIsEmpty() {
        new FriendsPage().checkTableFriendIsEmpty();
    }

    @Test
    @DisplayName("Проверка отображения кнопки Submit invitation")
    void submitInvitationVisible() {
        new FriendsPage().checkSubmitInvitationVisible();
    }

    @Test
    @DisplayName("Проверка отображения кнопки Decline invitation")
    void declineInvitationVisible() {
        new FriendsPage().checkDeclineInvitationVisible();
    }

}

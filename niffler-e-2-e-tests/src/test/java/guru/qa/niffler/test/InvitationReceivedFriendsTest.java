package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.User.UserType.INVITATION_SENT;

@ExtendWith(UsersQueueExtension.class)
public class InvitationReceivedFriendsTest {

    @BeforeEach()
    void openFriendsPage(@User(INVITATION_RECEIVED) UserJson user) {
        new WelcomePage().open().clickLoginButton()
                .authorize(user.username(), user.testData().password());
    }

    @Test
    @DisplayName("Проверка записей в таблице друзей")
    void checkTableFriendIsEmpty() {
        new MainPage().clickFriendsButton().checkTableFriendIsEmpty();
    }

    @Test
    @DisplayName("Проверка отображения в таблице текста Pending invitation")
    void textPendingInvitation() {
        new MainPage().clickAllPeopleButton().checkTextPendingInvitationVisible();
    }

}

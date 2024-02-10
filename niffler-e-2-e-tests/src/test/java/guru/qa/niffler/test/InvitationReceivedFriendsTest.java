package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;

@ExtendWith(UsersQueueExtension.class)
public class InvitationReceivedFriendsTest extends BaseWebTest {

    @BeforeEach()
    void openFriendsPage(@User(INVITATION_RECEIVED) UserJson user) {
        welcomePage
                .open()
                .clickLoginButton()
                .authorize(user.username(), user.testData().password());
    }

    @Test
    @DisplayName("Проверка записей в таблице друзей")
    void checkTableFriendIsEmpty() {
        mainPage
                .clickFriendsButton()
                .checkTableFriendIsEmpty();
    }

    @Test
    @DisplayName("Проверка отображения в таблице текста Pending invitation")
    void textPendingInvitation() {
        mainPage
                .clickAllPeopleButton()
                .checkTextPendingInvitationVisible();
    }

}

package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.*;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class InviteTestAnnotationOnly {
    static {
        Configuration.browserSize = "1980x1024";
    }

    private final LoginPage loginPage = new LoginPage();
    private final MenuElement menuBar = new MenuElement();

    @BeforeEach
    void doLogin() {
        LoginPage loginPage = Selenide.open("http://127.0.0.1:3000/main", LoginPage.class);
    }

    @Test
    @DisplayName("Есть отправленный запрос")
    void pendingInvitationShouldExist(@User(INVITATION_SEND) UserJson user) {
        loginPage.login(user.username(), user.testData().password());
        PeoplePage peoplePage = menuBar.clickAllPeopleButton();
        peoplePage.pendingInvitationExists();
    }

    @Test
    @DisplayName("Есть входящий запрос")
    void incomingInvitationShouldExist(@User(INVITATION_RECEIVED) UserJson user) {
        loginPage.login(user.username(), user.testData().password());
        FriendsPage friendsPage = menuBar.clickFriendsButton();
        friendsPage.friendInvitationExists();
    }

    @Test
    @DisplayName("В списке друзей есть друг")
    void friendShouldExist(@User(WITH_FRIENDS) UserJson user) {
        loginPage.login(user.username(), user.testData().password());
        FriendsPage friendsPage = menuBar.clickFriendsButton();
        friendsPage.friendExists();
    }
}

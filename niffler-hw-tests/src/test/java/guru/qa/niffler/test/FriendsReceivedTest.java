package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.REQUEST_RECEIVED;

@ExtendWith(UsersQueueExtension.class)
public class FriendsReceivedTest extends BaseWebTest {
    public WelcomePage welcomePage = new WelcomePage();
    public LoginPage loginPage = new LoginPage();
    public TopMenu topMenu = new TopMenu();
    public MainPage mainPage = new MainPage();
    public FriendsPage friendsPage = new FriendsPage();
    public PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin(@User(REQUEST_RECEIVED) UserJson user) {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void requestReceivedShouldBeDisplayed0(@User(REQUEST_RECEIVED) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }

    @Test
    void requestReceivedShouldBeDisplayed1(@User(REQUEST_RECEIVED) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }
}

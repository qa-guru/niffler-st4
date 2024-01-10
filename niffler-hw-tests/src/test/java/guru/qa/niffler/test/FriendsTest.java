package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest extends BaseWebTest {
    public WelcomePage welcomePage = new WelcomePage();
    public LoginPage loginPage = new LoginPage();
    public TopMenu topMenu = new TopMenu();
    public MainPage mainPage = new MainPage();
    public FriendsPage friendsPage = new FriendsPage();
    public PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin(@User(WITH_FRIENDS) UserJson user) {
        System.out.println("user0: " + user.username());
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(user.username(), user.testData().password());
    }

    @Test
    void friendsTableShouldNotBeEmpty0(@User(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsTableShouldNotBeEmpty1(@User(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsTableShouldNotBeEmpty2(@User(WITH_FRIENDS) UserJson user) throws Exception {
        Thread.sleep(3000);
    }

    @Test
    void friendsShouldBeDisplayed(@User(WITH_FRIENDS) UserJson user) {
        topMenu.clickFriendsTopMenu();
        friendsPage.friendShouldBeDisplayed();
    }

    @Test
    void testWithoutParam() throws Exception {

    }

    @Test
    void testWithSeveralParam(@User(REQUEST_SENT) UserJson userSent, @User(REQUEST_RECEIVED) UserJson userReceived) {
        System.out.println("user1: " + userSent.username());
        System.out.println("user2: " + userReceived.username());
    }
}

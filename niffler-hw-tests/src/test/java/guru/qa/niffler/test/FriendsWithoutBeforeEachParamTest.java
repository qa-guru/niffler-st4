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
public class FriendsWithoutBeforeEachParamTest extends BaseWebTest{
    public WelcomePage welcomePage = new WelcomePage();
    public LoginPage loginPage = new LoginPage();
    public TopMenu topMenu = new TopMenu();
    public MainPage mainPage = new MainPage();
    public FriendsPage friendsPage = new FriendsPage();
    public PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
    }

    @Test
    void friendsShouldBeDisplayed(@User(WITH_FRIENDS) UserJson user) throws Exception {
        loginPage.login(user.username(), user.testData().password());
        topMenu.clickFriendsTopMenu();
        friendsPage.friendShouldBeDisplayed();
    }

    @Test
    void requestSentShouldBeDisplayed(@User(REQUEST_SENT) UserJson user) throws Exception {
        loginPage.login(user.username(), user.testData().password());
        topMenu.clickPeopleTopMenu();
        peoplePage.requestSentShouldBeDisplayed();
    }

    @Test
    void requestReceivedShouldBeDisplayed(@User(REQUEST_RECEIVED) UserJson user) throws Exception {
        loginPage.login(user.username(), user.testData().password());
        topMenu.clickFriendsTopMenu();
        friendsPage.requestReceivedShouldBeDisplayed();
    }
}

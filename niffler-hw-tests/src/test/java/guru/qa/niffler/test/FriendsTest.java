package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest {
  public WelcomePage welcomePage = new WelcomePage();
  public LoginPage loginPage = new LoginPage();
  public MainPage mainPage = new MainPage();

  @BeforeEach
  void doLogin(@User(WITH_FRIENDS) UserJson user) {
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
}

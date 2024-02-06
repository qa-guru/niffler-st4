package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.test.pages.AllPeoplePage;
import guru.qa.niffler.test.pages.FriendsPage;
import guru.qa.niffler.test.pages.LoginPage;
import guru.qa.niffler.test.pages.MainPage;
import guru.qa.niffler.test.pages.WelcomePage;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

  WelcomePage welcomePage = new WelcomePage();
  LoginPage loginPage = new LoginPage();
  MainPage mainPage = new MainPage();
  FriendsPage friendsPage = new FriendsPage();
  AllPeoplePage allPeoplePage = new AllPeoplePage();
}

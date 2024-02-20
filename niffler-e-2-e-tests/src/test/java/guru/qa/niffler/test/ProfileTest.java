package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.page.message.SuccessMsg;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest {

  @DbUser
  @Test
  void avatarShouldBeDisplayedInHeader(UserAuthEntity userAuth) {
    Selenide.open(WelcomePage.URL, WelcomePage.class)
        .doLogin()
        .fillLoginPage(userAuth.getUsername(), userAuth.getPassword())
        .submit();

    new MainPage()
        .waitForPageLoaded()
        .getHeader()
        .toProfilePage()
        .setAvatar("images/duck.jpg")
        .submitProfile()
        .checkToasterMessage(SuccessMsg.PROFILE_UPDATED);

    new MainPage()
        .getHeader()
        .checkAvatar("images/duck.jpg");
  }
}

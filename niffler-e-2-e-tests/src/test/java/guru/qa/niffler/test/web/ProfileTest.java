package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.message.SuccessMsg;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest {

  @Test
  @ApiLogin(user = @TestUser)
  void avatarShouldBeDisplayedInHeader(@User() UserJson user) {
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

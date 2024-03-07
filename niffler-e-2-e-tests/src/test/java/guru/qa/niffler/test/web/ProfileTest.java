package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.TestUsers;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.message.SuccessMsg;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static guru.qa.niffler.jupiter.annotation.User.Point.OUTER;

public class ProfileTest extends BaseWebTest {

  @Test
  @TestUsers({
      @TestUser,
      @TestUser
  })
  @ApiLogin(user = @TestUser)
  void avatarShouldBeDisplayedInHeader(@User() UserJson user,
                                       @User(OUTER) UserJson[] outerUsers) {
    System.out.println(user);
    System.out.println(Arrays.toString(outerUsers));

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

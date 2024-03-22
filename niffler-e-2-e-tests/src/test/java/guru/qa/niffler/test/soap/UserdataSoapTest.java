package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Point.OUTER;

public class UserdataSoapTest extends BaseSoapTest {

  @TestUser
  @Test
  void currentUserInfoShouldContainsUsername(@User(OUTER) UserJson user) throws Exception {
    CurrentUserRequest currentUserRequest = new CurrentUserRequest();
    currentUserRequest.setUsername(user.username());

    final CurrentUserResponse response = userdataSoapClient.currentUser(currentUserRequest);
    Assertions.assertEquals(
        user.username(),
        response.getUser().getUsername()
    );
  }
}

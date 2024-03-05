package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.UserQueue;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserQueue.UserType.WITH_FRIENDS;

@ExtendWith({ContextHolderExtension.class, ApiLoginExtension.class, UsersQueueExtension.class})
public class FriendsTest extends BaseWebTest {

  @Test
  @ApiLogin(username = "duck", password = "12345")
  void friendsTableShouldNotBeEmpty0(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
    Selenide.open(FriendsPage.URL);
    System.out.println("");
  }

  @Test
  void friendsTableShouldNotBeEmpty1(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
    Thread.sleep(3000);
  }

  @Test
  void friendsTableShouldNotBeEmpty2(@UserQueue(WITH_FRIENDS) UserJson user) throws Exception {
    Thread.sleep(3000);
  }
}

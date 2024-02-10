package guru.qa.niffler.test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.MyDbUser;
import guru.qa.niffler.jupiter.DbUserCRUDExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DbUserCRUDExtension.class)
public class MyLoginTest extends BaseWebTest {


  @Test
  @MyDbUser(username = "Ivan", password = "12345")
  void statisticShouldBeVisibleAfterLoginWithDbUser(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userAuth.getUsername());
    $("input[name='password']").setValue(userAuth.getPassword());
    $("button[type='submit']").click();
    $(".main-content__section-stats").should(visible);

  }

  @Test
  @MyDbUser
  void statisticShouldBeVisibleAfterLoginWithEmptyDbUser(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userAuth.getUsername());
    $("input[name='password']").setValue(userAuth.getPassword());
    $("button[type='submit']").click();
    $(".main-content__section-stats").should(visible);
  }

}

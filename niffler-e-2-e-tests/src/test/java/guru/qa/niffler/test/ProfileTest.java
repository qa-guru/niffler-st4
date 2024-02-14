package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@ExtendWith(UserRepositoryExtension.class)
public class ProfileTest extends BaseWebTest {

  private UserRepository userRepository;

  private UserAuthEntity userAuth;
  private UserEntity user;

  @BeforeEach
  void createUser() {
    userAuth = new UserAuthEntity();
    userAuth.setUsername("valentin_10");
    userAuth.setPassword("12345");
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);

    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    userAuth.addAuthorities(authorities);

    user = new UserEntity();
    user.setUsername("valentin_10");
    user.setCurrency(CurrencyValues.RUB);
    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(user);
  }

  @AfterEach
  void removeUser() {
    userRepository.deleteInAuthById(userAuth.getId());
    userRepository.deleteInUserdataById(user.getId());
  }


  @Test
  void avatarShouldBeDisplayedInHeader() {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();

    new LoginPage()
        .setLogin(userAuth.getUsername())
        .setPassword(userAuth.getPassword())
        .submit();

    MainPage mainPage = new MainPage();
    mainPage.checkThatStatisticDisplayed();

    open(ProfilePage.PAGE_URL, ProfilePage.class)
        .addAvatar("images/duck.jpg");

    new MainPage()
        .checkAvatar("images/duck.jpg");
  }
}

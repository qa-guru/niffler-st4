package guru.qa.niffler.test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECIEVED;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class InvitationReceivedTest extends BaseWebTest {

  @BeforeEach
  void doLogin(@User(INVITATION_RECIEVED) UserJson user) {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword(user.username(),user.testData().password());
    mainPage.clickAllPeopleBtn();
  }

  @DisplayName("Проверим, что есть заявка в друзья и имя совпадает с ожидаемым")
  @Test
  void invitationTableShouldNotBeEmpty(@User(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.findRecordInFriendsTable(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть кнопка принять запрос в друзья")
  @Test
  void invitationTableShouldHaveSubmitInvitationBtn(@User(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.checkSubmitInvitationBtn(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть кнопка отклонить запрос в друзья")
  @Test
  void invitationTableShouldHaveDeclineInvitationBtn(@User(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.checkDeclineInvitationBtn(user.testData().friendName());
  }

  @DisplayName("Проверим, что количество заявок в друзья равно 1")
  @Test
  void invitationTableShouldHaveOneRecord() {
    allPeoplePage.checkCountInvitation(1);
  }

  @DisplayName("Проверим, что есть аватар")
  @Test
  void invitationTableShouldHaveAvatar(@User(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.checkAvatar(user.testData().friendName());
  }


}

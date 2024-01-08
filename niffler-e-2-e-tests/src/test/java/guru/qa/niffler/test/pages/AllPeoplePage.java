package guru.qa.niffler.test.pages;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;

public class AllPeoplePage {

  private final ElementsCollection invitationTable = $(".abstract-table tbody").$$("tr");

  public AllPeoplePage findRecordInFriendsTable (String text) {
    invitationTable.find(text(text)).shouldBe(visible);
    return this;
  }

  public AllPeoplePage checkPendingInvitation (String text) {
    invitationTable.find(text(text)).$(byText("Pending invitation")).shouldBe(visible);
    return this;
  }

  public AllPeoplePage checkCountInvitation(int count) {
    invitationTable.shouldHave(size(count));
    return this;
  }

  public AllPeoplePage checkAvatar(String text) {
    invitationTable.find(text(text)).$(".people__user-avatar").shouldBe(visible);
    return this;
  }

  public AllPeoplePage checkSubmitInvitationBtn (String text) {
    invitationTable.find(text(text)).$(".button-icon_type_submit").shouldBe(visible);
    return this;
  }

  public AllPeoplePage checkDeclineInvitationBtn (String text) {
    invitationTable.find(text(text)).$(".button-icon_type_close").shouldBe(visible);
    return this;
  }


}

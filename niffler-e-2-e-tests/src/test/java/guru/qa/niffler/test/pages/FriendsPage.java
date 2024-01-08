package guru.qa.niffler.test.pages;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;

public class FriendsPage {

  private final ElementsCollection friendsTable = $(".abstract-table tbody").$$("tr");

  public void findRecordInFriendsTable (String text) {
    friendsTable.find(text(text)).shouldBe(visible);
  }

  public void checkFriendsStatus (String text) {
    friendsTable.find(text(text)).$(byText("You are friends")).shouldBe(visible);
  }

  public void checkCountFriends (int count) {
    friendsTable.shouldHave(size(count));
  }

  public void checkAvatar(String text) {
    friendsTable.find(text(text)).$(".people__user-avatar").shouldBe(visible);
  }

  public void checkRemoveBtn(String text) {
    friendsTable.find(text(text)).$("[data-tooltip-id='remove-friend']").shouldBe(visible);
  }
}

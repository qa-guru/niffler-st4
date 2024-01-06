package guru.qa.niffler.test.pages;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class MainPage {

  private SelenideElement historyOfSpendings = $(".spendings-table tbody");

  private SelenideElement deleteSpendingBtn = $(byText("Delete selected"));

  public void deleteFirstRowHistoryOfSpendingsByDescription (String description) {
    getFirstRowHistoryOfSpendingsByDescription(description).click();
    deleteSpendingBtn.click();
  }

  public MainPage historyOfSpendingContentIsEmpty() {
    historyOfSpendings.$$("tr").shouldHave(size(0));
    return this;
  }

  private SelenideElement getFirstRowHistoryOfSpendingsByDescription(String description) {
    return historyOfSpendings.$$("tr")
        .find(text(description))
        .$$("td")
        .first();
  }

  private ElementsCollection getHistoryOfSpendingContent () {
    return historyOfSpendings.$$("tr");
  }
}

package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpendingTest extends BaseWebTest {

  static {
    Configuration.browserSize = "1980x1024";
  }

  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginLink();
    loginPage.login("duck", "12345");
  }

  @GenerateCategory(
          category = "Обучение",
          username = "duck"
  )
  @GenerateSpend(
      username = "duck",
      description = "QA.GURU Advanced 4",
      amount = 72500.00,
      category = "Обучение",
      currency = CurrencyValues.RUB
  )
  @DisabledByIssue("2")
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    mainPage.selectSpendingByDescription(spend.description());
    mainPage.clickDeleteSelectedButton();
    mainPage.spendingsTableShouldHaveSize(0);
  }
}

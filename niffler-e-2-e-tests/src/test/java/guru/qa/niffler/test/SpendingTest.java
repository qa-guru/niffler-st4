package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
@Feature("Spendings")
public class SpendingTest extends BaseWebTest {
  private final WelcomePage welcomePage = new WelcomePage();

  static {
    Configuration.browserSize = "1980x1024";
  }

  @GenerateCategory(
          category = "Обучение",
          username = "duck"
  )
  @GenerateSpend(
          username = "duck",
          description = "QA.GURU Advanced 4",
          amount = 72500.00,
          currency = CurrencyValues.RUB
  )
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    welcomePage.open().clickLoginAndGoToLoginPage()
            .loginInUser("duck", "12345")
            .deleteSpendingByButtonDelete(spend.description())
            .verifyEmptyListOfSpendings();
  }
}

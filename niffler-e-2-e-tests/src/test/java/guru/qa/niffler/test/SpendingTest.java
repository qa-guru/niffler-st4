package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SpendingTest {

  static {
    Configuration.browserSize = "1920x1080";
  }

  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
            .goToLoginPage()
            .doLogin("duck", "12345");
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
  @Test
  @DisplayName("Проверка удаления траты")
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {

    new MainPage()
            .selectSpendingByDescription(spend.description())
            .deleteSelectedSpendings()
            .checkThatSpendingsEmpty();
  }
}

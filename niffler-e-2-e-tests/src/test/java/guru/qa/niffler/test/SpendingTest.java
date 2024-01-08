package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.open;

public class SpendingTest {

  LoginPage loginPage;
  MainPage mainPage;

  static {
    Configuration.browserSize = "1980x1024";
  }

  @BeforeEach
  void doLogin() {
    loginPage = open("http://127.0.0.1:3000/main", LoginPage.class);
    mainPage = loginPage.doLogin("duck","12345");
  }

  @GenerateCategory(
          username = "duck",
          category = "Обучение"
  )
  @GenerateSpend(
      username = "duck",
      description = "QA.GURU Advanced 4",
      amount = 72500.00,
      category = "Обучение",
      currency = CurrencyValues.RUB
  )
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    mainPage.selectSpend(spend);

    Allure.step("Delete spending", () -> mainPage.pressDeleteSelected());

    Allure.step("Check that spending was deleted", () -> {
      mainPage
          .spendings()
          .shouldHave(size(0));
    });
  }
}

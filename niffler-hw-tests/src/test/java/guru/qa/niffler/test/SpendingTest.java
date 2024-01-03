package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.LoginPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpendingTest {
  public WelcomePage welcomePage = new WelcomePage();
  public LoginPage loginPage = new LoginPage();
  public MainPage mainPage = new MainPage();

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
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    mainPage.selectSpendingByDescription(spend.description());
    mainPage.clickDeleteSelectedButton();
    mainPage.spendingsTableShouldHaveSize(0);
  }
}

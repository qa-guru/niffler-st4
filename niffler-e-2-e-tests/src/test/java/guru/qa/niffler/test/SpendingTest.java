package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.test.pages.LoginPage;
import guru.qa.niffler.test.pages.MainPage;
import guru.qa.niffler.test.pages.WelcomePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTest {

  WelcomePage welcomePage = new WelcomePage();
  LoginPage loginPage = new LoginPage();
  MainPage mainPage = new MainPage();

  static {
    Configuration.browserSize = "1980x1024";
  }

  @GenerateCategory(
      username = "duck",
      category = "Наука"
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
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword("duck","12345");
    mainPage.deleteFirstRowHistoryOfSpendingsByDescription(spend.description());
    mainPage.getHistoryOfSpendingContent().shouldHave(size(0));

  }

}

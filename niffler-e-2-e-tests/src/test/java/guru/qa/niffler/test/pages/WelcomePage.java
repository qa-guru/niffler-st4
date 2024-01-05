package guru.qa.niffler.test.pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WelcomePage {

  private SelenideElement loginBtn = $("a:nth-child(1)");

  private SelenideElement registerBtn = $("a:nth-child(2)");

  public LoginPage clickLoginBtn() {
    loginBtn.click();
    return new LoginPage();
  }

  public void clickRegisterBtn() {
    registerBtn.click();
    //todo return RegisterPage
  }

}

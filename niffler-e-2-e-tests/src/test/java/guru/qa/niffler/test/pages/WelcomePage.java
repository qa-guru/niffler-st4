package guru.qa.niffler.test.pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WelcomePage {

  private SelenideElement loginBtn = $("a:nth-child(1)");

  private SelenideElement registerBtn = $("a:nth-child(2)");

  public WelcomePage clickLoginBtn() {
    loginBtn.click();
    return this;
  }

  public WelcomePage clickRegisterBtn() {
    registerBtn.click();
    return this;
    //todo return RegisterPage
  }

}

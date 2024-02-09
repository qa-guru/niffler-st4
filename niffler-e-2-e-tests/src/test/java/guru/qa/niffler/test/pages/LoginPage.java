package guru.qa.niffler.test.pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class LoginPage {

  private final SelenideElement userNameField =$("input[name='username']");

  private final SelenideElement passwordField= $("input[name='password']");

  private final SelenideElement signInBtn = $("button[type='submit']");

  public LoginPage loginByUserAndPassword(String user, String password) {
    userNameField.setValue(user);
    passwordField.setValue(password);
    signInBtn.click();
    return this;

  }

}
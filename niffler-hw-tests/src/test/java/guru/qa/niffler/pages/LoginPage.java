package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {
    private SelenideElement usernameField = $("input[name='username']");
    private SelenideElement passwordField = $("input[name='password']");
    private SelenideElement signInButton = $("button[type='submit']");

    @Step("Login '{username}' / '{password}'")
    public void login(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        signInButton.click();
    }

}

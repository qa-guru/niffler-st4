package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage {

    private SelenideElement usernameField = $("#username");
    private SelenideElement passwordField = $("#password");
    private SelenideElement passwordSubmitField = $("#passwordSubmit");
    private SelenideElement signUpButton = $("button[type='submit']");

    @Step("Register '{username}' / '{password}'")
    public void register(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        passwordSubmitField.setValue(password);
        signUpButton.click();
    }

}

package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement usernameField = $("input[name='username']");
    private SelenideElement passwordField = $("input[name='password']");
    private SelenideElement signInButton = $("button[type='submit']");

    public void login(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        signInButton.click();
    }
}

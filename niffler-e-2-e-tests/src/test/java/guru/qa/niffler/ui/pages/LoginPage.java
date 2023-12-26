package guru.qa.niffler.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement
            usernameField = $("input[name='username']"),
            passwordField = $("input[name='password']"),
            signInButton = $("button[type='submit']");

    public MainPage authorize(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        signInButton.click();
        new MainPage().checkThatMainPageSectionsAreVisible();

        return new MainPage();
    }
}

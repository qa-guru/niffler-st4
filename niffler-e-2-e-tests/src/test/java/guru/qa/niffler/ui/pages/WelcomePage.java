package guru.qa.niffler.ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.java.Log;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {
    private SelenideElement
            loginButton = $("a[href*='redirect']"),
            registerButton = $("a[href*='register']");

    public LoginPage clickLoginButton() {
        loginButton.click();

        return new LoginPage();
    }
}

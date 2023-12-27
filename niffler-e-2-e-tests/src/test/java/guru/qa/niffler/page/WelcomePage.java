package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {

    private final String loginButton = "a[href*='redirect']";
    private final String registerButton = "a[href*='auth']";

    @Step("Перейти к странице логина")
    public LoginPage goToLoginPage() {
        $(loginButton).click();
        return new LoginPage();
    }
}

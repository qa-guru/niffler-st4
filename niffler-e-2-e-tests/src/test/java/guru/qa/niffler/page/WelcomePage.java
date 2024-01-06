package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {
    private static final String MAIN_URL = "http://127.0.0.1:3000/main";
    private final SelenideElement
            loginButton = $("[href*='redirect']"),
            registerButton = $("[href*='register']"),
            title = $(withText("Welcome to magic journey with Niffler. The coin keeper")),
            logo = $("img.main__logo");

    @Step("Ожидание загрузки welcome страницы приложения")
    public WelcomePage waitUntilLoaded() {
        title.should(appear);
        logo.should(appear);
        return this;
    }

    @Step("Открыть welcome страницу приложения")
    public WelcomePage open() {
        Selenide.open(MAIN_URL);
        return this.waitUntilLoaded();
    }
    @Step("Нажать кнопку 'Login' и перейти на страницу авторизации")
    public LoginPage clickLoginAndGoToLoginPage() {
        loginButton.click();
        return new LoginPage().waitUntilLoaded();
    }

    @Step("Нажать кнопку 'Register' и перейти на страницу регистрации")
    public RegisterPage clickRegisterAndGoToRegisterPage() {
        registerButton.click();
        return new RegisterPage().waitUntilLoaded();
    }
}

package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement
            subTitle = $(withText("Registration form")),
            userNameInput = $("[name='username']"),
            passwordInput = $("[name='password']"),
            passwordSubmitInput = $("[name='passwordSubmit']"),
            signInButton = $("[type='submit']"),
            subTitleAfterRegister = $(withText("Congratulations! You've registered!")),
            loginButton = $("[href*='redirect']");

    @Step("Ожидание загрузки Register страницы приложения")
    public RegisterPage waitUntilLoaded() {
        subTitle.should(appear);
        return this;
    }

    @Step("Ожидание регистрации нового пользователя")
    public RegisterPage waitUntilLoadedRegistrationSuccess() {
        subTitleAfterRegister.should(appear);
        return this;
    }

    @Step("Зарегистрировать нового пользователя {userName}")
    public LoginPage RegisterNewUser(String userName, String password) {
        userNameInput.setValue(userName);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signInButton.click();
        waitUntilLoadedRegistrationSuccess();
        loginButton.click();
        return new LoginPage().waitUntilLoaded();
    }
}

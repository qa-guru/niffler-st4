package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement

            subTitle = $(withText("Please sign in")),
            userNameInput = $("[name='username']"),
            passwordInput = $("[name='password']"),
            signInButton = $("[type='submit']");

    @Step("Ожидание загрузки Login страницы приложения")
    public LoginPage waitUntilLoaded() {
        subTitle.should(appear);
        return this;
    }

    @Step("Залогиниться на главную страницу пользователем {userName}")
    public MainPage loginInUser(String userName, String password) {
        userNameInput.setValue(userName);
        passwordInput.setValue(password);
        signInButton.click();
        return new MainPage().waitUntilLoaded();
    }
}

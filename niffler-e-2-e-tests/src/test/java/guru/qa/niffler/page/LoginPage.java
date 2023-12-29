package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final static String HEADER_TEXT = "Welcome to Niffler. The coin keeper";

    private final SelenideElement
            header = $("h1[class=form__header]"),
            userNameInput = $("input[name='username']"),
            userPasswordInput = $("input[name='password']"),
            signInButton = $("button[type='submit']");

    @Step("Ожидать загрузку страницы")
    public LoginPage loadPage() {
        header.should(text(HEADER_TEXT));
        return this;
    }

    @Step("Заполнить поле UserName {userName}")
    public LoginPage setUserName(String userName) {
        userNameInput.setValue(userName);
        return this;
    }

    @Step("Заполнить поле Password {password}")
    public LoginPage setPassword(String password) {
        userPasswordInput.setValue(password);
        return this;
    }

    @Step("Нажать на кнопку Sign In")
    public LoginPage clickSignIn() {
        signInButton.click();
        return this;
    }
}

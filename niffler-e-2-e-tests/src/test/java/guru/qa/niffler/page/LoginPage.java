package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final String usernameField = "input[name='username']";
    private final String passwordField = "input[name='password']";
    private final String submitButton = "button[type='submit']";

    @Step("Авторизоваться под пользователем '{login}'")
    public MainPage doLogin(String login, String password) {
        $(usernameField).setValue(login);
        $(passwordField).setValue(password);
        $(submitButton).click();
        return new MainPage();
    }
}

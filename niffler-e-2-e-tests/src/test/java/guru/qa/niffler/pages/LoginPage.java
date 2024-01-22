package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
    private SelenideElement filedUsername = $("input[name='username']");
    private SelenideElement filedPassword = $("input[name='password']");
    private SelenideElement buttonSubmit = $("button[type='submit']");

    @Step("Авторизоваться в системе с userName = {userName} и password={password}")
    public MainPage authorize(String userName, String password) {
        setUserName(userName);
        setPassword(password);
        clickButtonSubmit();
        return new MainPage();
    }

    @Step("Заполнить поле Username {userName}")
    public LoginPage setUserName(String userName) {
        filedUsername.setValue(userName);
        return this;
    }

    @Step("Заполнить поле Password {password}")
    public LoginPage setPassword(String password) {
        filedPassword.setValue(password);
        return this;
    }

    @Step("Нажать кнопку Sign up")
    public LoginPage clickButtonSubmit() {
        buttonSubmit.click();
        return this;
    }

}

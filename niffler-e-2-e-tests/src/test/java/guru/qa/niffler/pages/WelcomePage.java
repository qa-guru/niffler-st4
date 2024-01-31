package guru.qa.niffler.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class WelcomePage {
    private SelenideElement buttonLogin = $x("//*[@href='/redirect']");

    @Step("Нажать на кнопку Login")
    public LoginPage clickLoginButton() {
        buttonLogin.click();
        return new LoginPage();
    }

    @Step("Открыть старовую старницу")
    public WelcomePage open() {
        Selenide.open("http://127.0.0.1:3000/main");
        return this;
    }

}

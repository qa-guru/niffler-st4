package guru.qa.niffler.pages;

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

}

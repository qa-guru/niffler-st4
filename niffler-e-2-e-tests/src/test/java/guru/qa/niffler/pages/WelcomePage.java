package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {

    private SelenideElement buttonLogin = $("a[href*='redirect']");


    @Step("Клик на кнопку Login")
    public LoginPage clickLoginButton() {
        buttonLogin.click();
        return new LoginPage();
    }
}

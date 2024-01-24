package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage {

    private final SelenideElement loginButton = $("a[href*='redirect']");

    @Step("Нажать кнопку [Login]")
    public void clickLoginButton() {
        loginButton.click();
    }
}
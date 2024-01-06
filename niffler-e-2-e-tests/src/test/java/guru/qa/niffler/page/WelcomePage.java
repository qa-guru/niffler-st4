package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class WelcomePage {
    private static final String URL = "http://127.0.0.1:3000/main";
    private static final String HEADER_TEXT = "Welcome to magic journey with Niffler. The coin keeper";

    private final SelenideElement
            header = $("h1[class=main__header]"),
            loginButton = $("a[href*='redirect']");

    @Step("Открыть страницу и ожидать загрузку")
    public WelcomePage openPage() {
        open(URL);
        header.should(text(HEADER_TEXT));
        return this;
    }

    @Step("Нажать на кнопку Login")
    public LoginPage clickLogin() {
        loginButton.click();
        return new LoginPage();
    }
}

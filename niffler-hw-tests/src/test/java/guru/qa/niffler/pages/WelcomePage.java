package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage {

    private SelenideElement loginLink = $("a[href*='redirect']");
    private SelenideElement registerLink = $(byText("Register"));

    @Step("Click Login link")
    public void clickLoginLink() {
        loginLink.click();
    }

    @Step("Click Register link")
    public void clickRegisterLink() {
        registerLink.click();
    }

    @Step("Login link should be displayed")
    public void loginLinkShouldBeDisplayed(){
        loginLink.shouldBe(Condition.visible);
    }
}

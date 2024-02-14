package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.pages.message.Msg;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected final SelenideElement toaster = $(".Toastify__toast-body");

    @SuppressWarnings("unchecked")
    @Step("Check message '{msg}'")
    public void checkMessage(Msg msg) {
        toaster.shouldHave(text(msg.getMessage()));
    }

}

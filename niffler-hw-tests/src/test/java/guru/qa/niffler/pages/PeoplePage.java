package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {
    private SelenideElement people = $(".people-content tbody");

    public void requestSentShouldBeDisplayed() {
        people.$(byText("Pending invitation")).shouldBe(visible);
    }
}

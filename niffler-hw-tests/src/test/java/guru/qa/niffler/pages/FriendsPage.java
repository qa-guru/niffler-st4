package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
    private SelenideElement friends = $(".people-content tbody");

    public void requestReceivedShouldBeDisplayed() {
        friends.$("[data-tooltip-id=submit-invitation]").shouldBe(visible);
    }

    public void friendShouldBeDisplayed() {
        friends.$(byText("You are friends")).shouldBe(visible);
    }
}

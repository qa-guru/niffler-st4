package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class PeoplePage extends BasePage {
    private SelenideElement people = $(".people-content tbody");

    private ElementsCollection users = $$x("//tbody/tr/td[2]");
    private ElementsCollection actions = $$x("//tbody/tr/td[4]");

    @Step("Click Add Friend '{username}'")
    public void clickAddFriend(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (users.get(i).text().equals(username)) {
                actions.get(i).click();
                return;
            }
        }
    }


    public void requestSentShouldBeDisplayed() {
        people.$(byText("Pending invitation")).shouldBe(visible);
    }
}

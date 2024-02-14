package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class FriendsPage extends BasePage {
    private SelenideElement friends = $(".people-content tbody");

    private ElementsCollection users = $$x("//tbody/tr/td[2]");
    private ElementsCollection actions = $$x("//tbody/tr/td[4]");

    @Step("Submit invitation '{username}'")
    public void submitInvitation(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (users.get(i).text().equals(username)) {
                actions.get(i).find("div[data-tooltip-id='submit-invitation'] button").click();
                return;
            }
        }
    }

    @Step("Decline invitation '{username}'")
    public void declineInvitation(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (users.get(i).text().equals(username)) {
                actions.get(i).find("div[data-tooltip-id='decline-invitation'] button").click();
                return;
            }
        }
    }

    @Step("Remove friend '{username}'")
    public void removeFriend(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (users.get(i).text().equals(username)) {
                actions.get(i).find("div[data-tooltip-id='remove-friend'] button").click();
                return;
            }
        }
    }


    public void requestReceivedShouldBeDisplayed() {
        friends.$("[data-tooltip-id=submit-invitation]").shouldBe(visible);
    }

    public void friendShouldBeDisplayed() {
        friends.$(byText("You are friends")).shouldBe(visible);
    }
}

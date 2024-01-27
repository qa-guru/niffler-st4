package guru.qa.niffler.page_element;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderMainPageElement {

    private final SelenideElement
            main = $("[data-tooltip-id='main']"),
            friends = $("[data-tooltip-id='friends']"),
            people = $("[data-tooltip-id='people']"),
            profile = $("[data-tooltip-id='profile']"),
            logout = $("[data-tooltip-id='logout']"),
            invitationNotification = $(".header__sign");

    public HeaderMainPageElement waitUntilLoaded() {
        profile.should(appear);
        return this;
    }

    public HeaderMainPageElement notificationIsVisible() {
        invitationNotification.shouldBe(visible);
        return this;
    }

    public HeaderMainPageElement clickFriendsPage() {
        friends.click();
        return this;
    }

    public HeaderMainPageElement clickPeoplePage() {
        people.click();
        return this;
    }

    public HeaderMainPageElement clickProfilePage() {
        profile.click();
        return this;
    }

    public HeaderMainPageElement clickMainPage() {
        main.click();
        return this;
    }
}

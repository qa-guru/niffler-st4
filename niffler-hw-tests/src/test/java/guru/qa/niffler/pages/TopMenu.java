package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TopMenu {
    private SelenideElement friendsTopMenu = $("li[data-tooltip-id=friends]");
    private SelenideElement peopleTopMenu = $("li[data-tooltip-id=people]");

    public void clickFriendsTopMenu() {
        friendsTopMenu.click();
    }

    public void clickPeopleTopMenu() {
        peopleTopMenu.click();
    }
}

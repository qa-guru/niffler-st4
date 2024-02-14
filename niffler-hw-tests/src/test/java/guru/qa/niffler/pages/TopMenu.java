package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class TopMenu extends BasePage {
    private SelenideElement mainTopMenu = $("li[data-tooltip-id=main]");
    private SelenideElement friendsTopMenu = $("li[data-tooltip-id=friends]");
    private SelenideElement peopleTopMenu = $("li[data-tooltip-id=people]");
    private SelenideElement profileTopMenu = $("li[data-tooltip-id=profile]");
    private SelenideElement logout = $(".button-icon_type_logout");

    @Step("Click Main top menu")
    public void clickMainTopMenu() {
        mainTopMenu.click();
    }

    @Step("Click Friends top menu")
    public void clickFriendsTopMenu() {
        friendsTopMenu.click();
    }

    @Step("Click People top menu")
    public void clickPeopleTopMenu() {
        peopleTopMenu.click();
    }

    @Step("Click Profile top menu")
    public void clickProfileTopMenu() {
        profileTopMenu.click();
    }

    @Step("Click Logout")
    public void clickLogoutButton() {
        logout.click();
    }

    @Step("Logout is displayed")
    public boolean logoutIsDisplayed() {
        return logout.isDisplayed();
    }

}

package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MenuElement {
    private final SelenideElement allPeopleButton = $("a[href='/people']");
    private final SelenideElement friendsButton = $("a[href='/friends']");

    public PeoplePage clickAllPeopleButton() {
        allPeopleButton.click();
        return new PeoplePage();
    }
    public FriendsPage clickFriendsButton() {
        friendsButton.click();
        return new FriendsPage();
    }
}
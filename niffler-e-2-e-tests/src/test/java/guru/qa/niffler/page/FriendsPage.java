package guru.qa.niffler.page;

import guru.qa.niffler.page_element.HeaderMainPageElement;
import guru.qa.niffler.page_element.PeopleTableElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class FriendsPage {
    private final PeopleTableElement peopleTableElement = new PeopleTableElement();
    private final HeaderMainPageElement headerMainPageElement = new HeaderMainPageElement();

    @Step("Ожидание загрузки страницы friends")
    public FriendsPage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("friends"));
        return this;
    }

    @Step("Проверка непустого списка друзей")
    public FriendsPage verifyThatFriendsListNotEmpty() {
        peopleTableElement.verifyFriendsListNotEmpty();
        return this;
    }

    @Step("Проверка наличия приглашения дружить")
    public FriendsPage verifyReceivedFriendsInvitation() {
        headerMainPageElement.notificationIsVisible();
        peopleTableElement.verifyReceivedFriendsInvitation();

        return this;
    }

    @Step("Проверка наличия кнопки 'Remove Friend'")
    public FriendsPage verifyExistingButtonRemoveFriend() {
        peopleTableElement.verifyExistingButtonRemoveFriend();
        return this;
    }

    @Step("Перейти на страницу профиля")
    public ProfilePage goToProfile() {
        headerMainPageElement.clickProfilePage();
        return new ProfilePage().waitUntilLoaded();
    }

    @Step("Перейти на страницу people")
    public PeoplePage goToPeoplePage() {
        headerMainPageElement.clickPeoplePage();
        return new PeoplePage().waitUntilLoaded();
    }

    @Step("Перейти на главную страницу ")
    public MainPage goToMainPage() {
        headerMainPageElement.clickMainPage();
        return new MainPage().waitUntilLoaded();
    }
}

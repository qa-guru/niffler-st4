package guru.qa.niffler.page;

import guru.qa.niffler.page_element.HeaderMainPageElement;
import guru.qa.niffler.page_element.ProfileInformationElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProfilePage {
    private final ProfileInformationElement profileInformationElement = new ProfileInformationElement();
    private final HeaderMainPageElement headerMainPageElement = new HeaderMainPageElement();

    @Step("Ожидание загрузки страницы профиля")
    public ProfilePage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("profile"));
        return this;
    }

    @Step("Проверка имени профиля {username}")
    public ProfilePage verifyProfileName(String username) {
        profileInformationElement.verifyProfileName(username);
        return this;
    }

    @Step("Перейти на страницу people")
    public PeoplePage goToPeoplePage() {
        headerMainPageElement.clickPeoplePage();
        return new PeoplePage().waitUntilLoaded();
    }

    @Step("Перейти на страницу friends")
    public FriendsPage goToFriendsPage() {
        headerMainPageElement.clickFriendsPage();
        return new FriendsPage().waitUntilLoaded();
    }

    @Step("Перейти на главную страницу ")
    public MainPage goToMainPage() {
        headerMainPageElement.clickMainPage();
        return new MainPage().waitUntilLoaded();
    }
}

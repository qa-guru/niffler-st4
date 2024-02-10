package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.page_element.HeaderMainPageElement;
import guru.qa.niffler.page_element.ProfileInformationElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProfilePage {
    private final ProfileInformationElement profileInformationElement = new ProfileInformationElement();
    private final HeaderMainPageElement headerMainPageElement = new HeaderMainPageElement();
    private final SelenideElement toast = $("[class*='toast']");

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

    @Step("Перезагрузить страницу Профиля")
    public ProfilePage refreshProfilePage() {
        refresh();
        return this;
    }

    @Step("Проверить дефолтное значение Currency {currencyValue}")
    public ProfilePage verifyDefaultCurrencyValue(CurrencyValues currencyValue) {
        profileInformationElement.verifyDefaultCurrencyValue(currencyValue);
        return this;
    }

    @Step("Задать Имя и Фамилию пользователю")
    public ProfilePage setFirstAndSurName(String firstName, String surName) {
        profileInformationElement.setFirstAndSurName(firstName, surName);
        toast.shouldHave(text("Profile successfully updated"));
        return this;
    }
}

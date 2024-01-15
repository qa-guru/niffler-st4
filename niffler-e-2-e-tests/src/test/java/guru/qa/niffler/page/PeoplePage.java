package guru.qa.niffler.page;

import guru.qa.niffler.page_element.HeaderMainPageElement;
import guru.qa.niffler.page_element.PeopleTableElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class PeoplePage {
    private final PeopleTableElement peopleTableElement = new PeopleTableElement();
    private final HeaderMainPageElement headerMainPageElement = new HeaderMainPageElement();

    @Step("Ожидание загрузки страницы people")
    public PeoplePage waitUntilLoaded() {
        webdriver().shouldHave(urlContaining("people"));
        return this;
    }

    @Step("Проверка наличия отправленного запроса дружить")
    public PeoplePage verifyPendingFriendsInvitation() {
        peopleTableElement.verifyPendingFriendsInvitation();
        return this;
    }

    @Step("Перейти на страницу профиля")
    public ProfilePage goToProfile() {
        headerMainPageElement.clickProfilePage();
        return new ProfilePage().waitUntilLoaded();
    }

    @Step("Перейти на страницу friends")
    public FriendsPage goToFriendsPage() {
        headerMainPageElement.clickFriendsPage();
        return new FriendsPage().waitUntilLoaded();
    }

    @Step("Перейти на главную страницу")
    public MainPage goToMainPage() {
        headerMainPageElement.clickMainPage();
        return new MainPage().waitUntilLoaded();
    }

    @Step("Проверить отправленное приглашение пользователю {userName}")
    public PeoplePage verifyPendingInvitationToUser(String username) {
        peopleTableElement.verifyPendingInvitationToUser(username);
        return this;
    }

    @Step("Проверить наличие кнопки отклонить дружбу")
    public PeoplePage verifyExistingDeclineInvitationBtn() {
        peopleTableElement.verifyExistingDeclineInvitationBtn();
        return this;
    }
}

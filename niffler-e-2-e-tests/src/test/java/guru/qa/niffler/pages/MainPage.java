package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Data;
import org.hibernate.annotations.Comment;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeLessThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private SelenideElement table = $x("//table");
    private SelenideElement deleteButton = $(byText("Delete selected"));
    private SelenideElement friendsButton = $x("//a[@href='/friends']");
    private SelenideElement peopleButton = $x("//a[@href='/people']");

    @Step("В таблице выделить строку с текстом {expectText}")
    public MainPage selectRowInTableByText(String expectText) {
        table.$$("tr").find(text(expectText))
                .$("td")
                .scrollTo()
                .click();
        return this;
    }

    @Step("Нажать кнопку Delete selected")
    public MainPage clickDeleteButton() {
        deleteButton.click();
        return this;
    }

    @Step("Проверить что таблица не содержит записей")
    public MainPage checkTableIsEmpty() {
        table.$$("tr").shouldHave(sizeLessThanOrEqual(1));
        return this;
    }

    @Step("Нажать кнопку Friends")
    public FriendsPage clickFriendsButton() {
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Нажать кнопку All people")
    public AllPeoplePage clickAllPeopleButton() {
        peopleButton.click();
        return new AllPeoplePage();
    }
}

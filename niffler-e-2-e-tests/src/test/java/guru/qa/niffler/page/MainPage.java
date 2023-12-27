package guru.qa.niffler.page;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final String spendingTable = ".spendings-table tbody";

    @Step("Выбрать трату по описанию '{description}'")
    public MainPage selectSpendingByDescription(String description) {
        $(spendingTable)
                .$$("tr")
                .find(text(description))
                .$$("td")
                .first()
                .scrollTo()
                .click();
        return this;
    }

    @Step("Удалить выбранные траты")
    public MainPage deleteSelectedSpendings() {
        $(byText("Delete selected"))
                .click();
        return this;
    }

    @Step("Убедиться, что траты отсутствуют")
    public MainPage checkThatSpendingsEmpty() {
        $(spendingTable)
                .$$("tr")
                .shouldHave(size(0));
        return this;
    }
}

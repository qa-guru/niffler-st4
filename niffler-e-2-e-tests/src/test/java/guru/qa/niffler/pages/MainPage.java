package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection spendingsTableRows = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deleteSelectedButton = $(byText("Delete selected"));

    @Step("Выбрать трату [{description}]")
    public MainPage selectSpendingByDescription(String description) {
        spendingsTableRows
                .findBy(text(description))
                .$("td")
                .scrollIntoView(true)
                .click();
        return this;
    }

    @Step("Нажать кнопку Delete selected")
    public MainPage clickDeleteSelectedButton() {
        deleteSelectedButton.click();
        return this;
    }

    @Step("В таблице с тратами количество строк равно [{size}]")
    public MainPage checkSpendingsTableRowsHasSize(int size) {
        spendingsTableRows.shouldHave(size(size));
        return this;
    }
}
package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeLessThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement spendingTable = $(".spendings-table tbody");
    private final SelenideElement buttonDeleteSpendingTable = $(byText("Delete selected"));

    public MainPage selectSpendingElement(String spendDescription) {
        SelenideElement trElement = spendingTable.$$("tr")
                .find(text(spendDescription));
        SelenideElement tdElement = trElement.$("td");
        tdElement.scrollIntoView(true);
        tdElement.click();
        return this;
    }

    public MainPage deleteSpendingByButton() {
        buttonDeleteSpendingTable.click();
        return this;
    }

    public MainPage checkSpendingElementDisappear() {
        spendingTable.$$("tr")
                .shouldHave(size(0));
        return this;
    }
}

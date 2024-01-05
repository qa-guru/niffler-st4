package guru.qa.niffler.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SpendingPages {

    @Step("click first spending in table by description {spendingDescription}")
    public void clickFirstSpendingByDescription(String spendingDescription) {
        $(".spendings-table tbody")
                .$$("tr")
                .find(text(spendingDescription))
                .$$("td")
                .first()
                .click();
    }

    @Step("check spending table size is zero")
    public void checkSpendingTableSizeEqualsZero() {
        $(".spendings-table tbody")
                .$$("tr")
                .shouldHave(size(0));
    }
}
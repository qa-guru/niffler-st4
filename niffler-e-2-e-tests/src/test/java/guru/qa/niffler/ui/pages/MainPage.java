package guru.qa.niffler.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private SelenideElement
            spendingsTable = $(".spendings-table"),
            spendingForm = $(".add-spending__form"),
            statisticsGraph = $(".main-content__section-stats");

    private ElementsCollection
            spendingsRows = spendingsTable.$$("tr"),
            spendingsButtons = $$(".spendings__table-controls button");

    private SelenideElement
            deleteSelectedButton = spendingsButtons.findBy(text("Delete selected"));

    public MainPage checkThatMainPageSectionsAreVisible() {
        spendingsTable.shouldBe(Condition.visible);
        spendingForm.shouldBe(Condition.visible);
        statisticsGraph.shouldBe(Condition.visible);

        return this;
    }

    public MainPage selectSpendingWithDescription(String description) {
        spendingsRows.findBy(text(description)).$("td").click();

        return this;
    }

    public MainPage clickDeleteSelectedButton() {
        deleteSelectedButton.click();

        return this;
    }

    public MainPage spendingRowsCountShouldBeEqualTo(int equalTo) {
        spendingsRows.shouldHave(size(equalTo));

        return this;
    }

}

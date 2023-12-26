package guru.qa.niffler.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement
            spendingsTable = $(".spendings-table");
    private final SelenideElement spendingForm = $(".add-spending__form");
    private final SelenideElement statisticsGraph = $(".main-content__section-stats");

    private final ElementsCollection
            spendingsRows = spendingsTable.$$("tbody tr");
    private final ElementsCollection spendingsButtons = $$(".spendings__table-controls button");

    private final SelenideElement
            deleteSelectedButton = spendingsButtons.findBy(text("Delete selected"));

    public MainPage checkThatMainPageSectionsAreVisible() {
        spendingsTable.shouldBe(Condition.visible);
        spendingForm.shouldBe(Condition.visible);
        statisticsGraph.shouldBe(Condition.visible);

        return this;
    }

    public MainPage selectSpendingWithDescription(String description) {
        spendingsRows.findBy(text(description)).$("td").scrollIntoView(true).click();

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

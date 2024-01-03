package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private ElementsCollection spendings = $(".spendings-table tbody").$$("tr");
    private SelenideElement deleteSelectedButton = $(byText("Delete selected"));

    public void selectSpendingByDescription(String description) {
        spendings.find(text(description))
                .$$("td")
                .first()
                .scrollTo()
                .click();
    }

    public void clickDeleteSelectedButton() {
        deleteSelectedButton.click();
    }

    public void numberOfSpendingsShouldBe(int spendingsSize) {
        spendings.shouldHave(size(spendingsSize));
    }
}

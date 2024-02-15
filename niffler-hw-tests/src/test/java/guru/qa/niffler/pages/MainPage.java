package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.pages.component.SpendingTable;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.condition.PhotoCondition.photoFromClasspath;

public class MainPage extends BasePage {
    private ElementsCollection spendings = $(".spendings-table tbody").$$("tr");
    private SelenideElement deleteSelectedButton = $(byText("Delete selected"));

    private SelenideElement categoryField = $x("//input[contains(@id, 'react-select')]");
    private ElementsCollection categoryValues = $$x("//div[@aria-disabled='false' and contains(@id, 'react-select')]");

    private SelenideElement amountField = $("input[name='amount']");
    private SelenideElement dateField = $(".calendar-wrapper input");
    private SelenideElement descriptionField = $("input[name='description']");

    private SelenideElement addSpendingButton = $("button[type='submit']");

    DateTimeFormatter spendingDateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SelenideElement avatar = $(".header__avatar");
    private final SpendingTable spendingTable = new SpendingTable();

    @Step("Add spending {category} / {amount} / {description}")
    public void addSpending(String category, String amount, LocalDate date, String description) {
        categoryField.click();
        categoryField.setValue(category);
        categoryValues.findBy(Condition.text(category)).click();

        amountField.setValue(amount);

        dateField.clear();
        dateField.setValue(spendingDateformatter.format(date));

        descriptionField.setValue(description);

        addSpendingButton.click();
    }

    @Step("Select spending by description '{description}'")
    public void selectSpendingByDescription(String description) {
        spendings.find(text(description))
                .$("td")
                .scrollTo()
                .click();
    }

    @Step("Click Delete Selected button")
    public void clickDeleteSelectedButton() {
        deleteSelectedButton.scrollTo();
        deleteSelectedButton.click();
    }

    @Step("Spendings table should have size '{expectedSize}'")
    public void spendingsTableShouldHaveSize(int expectedSize) {
        spendings.shouldHave(size(expectedSize));
    }

    @Step("check avatar")
    public void checkAvatar(String imageName) {
        avatar.shouldHave(photoFromClasspath(imageName));
    }

    public SpendingTable getSpendingTable() {
        return spendingTable;
    }

}

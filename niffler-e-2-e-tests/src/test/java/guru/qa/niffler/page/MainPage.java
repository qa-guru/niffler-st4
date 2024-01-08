package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page_element.HistoryOfSpendingsElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final HistoryOfSpendingsElement HistoryOfSpendingsElement = new HistoryOfSpendingsElement();
    private final SelenideElement
            toast = $("[class*='toast']");

    @Step("Ожидание загрузки главной страницы")
    public MainPage waitUntilLoaded() {
        HistoryOfSpendingsElement.waitUntilLoaded();
        return this;
    }

    @Step("Удалить Spending по названию {spendDescription}")
    public MainPage deleteSpendingByButtonDelete(String spendDescription) {
        HistoryOfSpendingsElement.deleteSpendingByButtonDeleteSpending(spendDescription);
        toast.shouldHave(text("Spendings deleted"));
        return this;
    }

    @Step("Проверить пустой список Spendings")
    public MainPage verifyEmptyListOfSpendings() {
        HistoryOfSpendingsElement.verifyEmptyListOfSpendings();
        return this;
    }
}

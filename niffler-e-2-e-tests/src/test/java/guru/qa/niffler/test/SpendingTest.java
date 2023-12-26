package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.jupiter.SpendExtension;
import guru.qa.niffler.jupiter.SpendResolverExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SpendingTest extends BaseTest {

    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            category = "Обучение",
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        open("http://127.0.0.1:3000/main");
        mainPage
                .selectSpendingWithDescription(spend.description())
                .clickDeleteSelectedButton()
                .spendingRowsCountShouldBeEqualTo(0);
    }
}

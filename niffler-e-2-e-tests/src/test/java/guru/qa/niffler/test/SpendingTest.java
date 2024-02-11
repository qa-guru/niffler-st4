package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

@Feature("Spendings")
public class SpendingTest extends BaseWebTest {
    @GenerateCategory(
            category = "Обучение",
            username = "duck"
    )
    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        welcomePage.open().clickLoginAndGoToLoginPage()
                .loginInUser("duck", "12345")
                .deleteSpendingByButtonDelete(spend.description())
                .verifyEmptyListOfSpendings();
    }
}

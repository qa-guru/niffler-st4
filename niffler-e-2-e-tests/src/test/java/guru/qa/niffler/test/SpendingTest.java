package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.WelcomePage;
import org.junit.jupiter.api.Test;


public class SpendingTest {

    private final String userName = "duck";

    @GenerateCategory(
            username = userName,
            category = "укуукук"
    )
    @GenerateSpend(
            username = userName,
            description = "Белый кролик",
            amount = 96500.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        Selenide.open("http://127.0.0.1:3000/main", WelcomePage.class)
                .clickLoginButton()
                .authorize(userName, "12345")
                .selectRowInTableByText(spend.category())
                .clickDeleteButton()
                .checkTableIsEmpty();
    }
}

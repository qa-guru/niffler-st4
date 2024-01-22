package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.DisabledByIssue;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.WelcomePage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTest extends BaseWebTest {

    static {
        Configuration.browserSize = "1920х1080";
    }

    private final String userName = "duck";

    @GenerateCategory(
            username = userName,
            category = "Игровая"
    )
    @GenerateSpend(
            username = userName,
            description = "Starfield",
            amount = 92500.00,
            currency = CurrencyValues.RUB
    )

    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        Selenide.open("http://127.0.0.1:3000/main", WelcomePage.class)
                .clickLoginButton()
                .authorize(userName, "12345")
                .selectSpendingElement(spend.description())
                .deleteSpendingByButton()
                .checkSpendingElementDisappear();
    }
}

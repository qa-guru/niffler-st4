package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Test;

public class SpendingDatabaseTest extends BaseWebTest {

    private SpendingRepository repository = new SpendingRepositoryHibernate();

    @GenerateSpend(
            username = "duck",
            description = "hello",
            amount = 125,
            category = "Обучение",
            currency = CurrencyValues.EUR
    )
    @Test
    void testSpendingDb(SpendJson spend) {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login("duck", "12345");

        mainPage.selectSpendingByDescription(spend.description());
        mainPage.clickDeleteSelectedButton();
        mainPage.spendingsTableShouldHaveSize(0);
    }
}

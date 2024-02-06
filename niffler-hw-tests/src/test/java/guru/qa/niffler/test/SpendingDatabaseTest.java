package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositoryHibernate;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class SpendingDatabaseTest extends BaseWebTest {

    private SpendingRepository repository = new SpendingRepositoryHibernate();

    SpendEntity spend;
    CategoryEntity category;

    @BeforeEach
    void beforeEach() {
        category = new CategoryEntity();
        category.setId(UUID.fromString("d3b3e113-0d1c-4b27-bcd6-21baa7cdd75e"));

        spend = new SpendEntity();
        spend.setUsername("duck");
        spend.setSpendDate(new Date());
        spend.setCurrency(CurrencyValues.EUR);
        spend.setAmount(5d);
        spend.setDescription("hello");
        spend.setCategory(category);

        repository.createSpending(spend);
    }

    @Test
    void testSpendingDb() {
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login("duck", "12345");

        mainPage.selectSpendingByDescription(spend.getDescription());
        mainPage.clickDeleteSelectedButton();
        mainPage.spendingsTableShouldHaveSize(0);
    }
}

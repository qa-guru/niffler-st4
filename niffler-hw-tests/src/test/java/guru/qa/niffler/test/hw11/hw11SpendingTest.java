package guru.qa.niffler.test.hw11;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.message.SuccessMsg;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

public class hw11SpendingTest extends BaseWebTest {

    private final static String tester1 = "tester1";
    private final static String tester2 = "tester2";

    @BeforeEach
    public void beforeEach(){
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(tester1, "12345");
    }

    @Test
    void addSpending() {

        mainPage.addSpending("testCategory1", "120", LocalDate.of(2023, 1, 25), "someText");

        mainPage.checkMessage(SuccessMsg.SPENDING_ADDED_MSG);

    }

    @GenerateSpend(
            username = tester1,
            description = "",
            amount = 100.00,
            category = "testCategory1",
            currency = CurrencyValues.USD
    )
    @Test
    void removeSpending(SpendJson spend) throws IOException {

        mainPage.selectSpendingByDescription(spend.description());
        mainPage.clickDeleteSelectedButton();

        mainPage.checkMessage(SuccessMsg.SPENDING_DELETED_MSG);
    }

}

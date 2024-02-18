package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.message.SuccessMsg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserRepositoryExtension.class)
public class DbUserTest extends BaseWebTest {

    private final String testUser = "testUser";
    private final String testCategory = "testCategory";

    @DbUser(username = testUser, password = "12345")
    @GenerateCategory(
            category = testCategory,
            username = testUser
    )
    @GenerateSpend(
            username = testUser,
            description = "",
            amount = 125,
            category = testCategory,
            currency = CurrencyValues.EUR
    )
    @Test
    void testSpendingDb(UserAuthEntity userAuth, SpendJson spend) {
        Selenide.open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login(userAuth.getUsername(), userAuth.getPassword());

        mainPage.selectSpendingByDescription(spend.description());
        mainPage.clickDeleteSelectedButton();
        mainPage.checkMessage(SuccessMsg.SPENDING_DELETED_MSG);
    }
}

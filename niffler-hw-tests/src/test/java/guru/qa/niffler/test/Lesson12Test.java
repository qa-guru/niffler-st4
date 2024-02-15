package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.message.SuccessMsg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class Lesson12Test extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        open(CFG.frontUrl());
        welcomePage.clickLoginLink();
        loginPage.login("duck", "12345");
    }

    @GenerateSpend(
            username = "duck",
            description = "",
            amount = 72500.00,
            category = "Обучение",
            currency = CurrencyValues.RUB
    )
    @Test
    void spendTableValuesAreCorrect(SpendJson spend) {

        mainPage.getSpendingTable().checkSpends(spend);

    }

    @Test
    void avatarShouldBeDisplayedInHeader() {

        topMenu.clickProfileTopMenu();

        profilePage.addAvatar("images/duck.jpg");

        profilePage.checkMessage(SuccessMsg.PROFILE_MSG);

        mainPage.checkAvatar("images/duck.jpg");

    }
}

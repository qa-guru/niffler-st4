package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friendTable = $(".abstract-table tbody");

    @Step("Проверка наличия строки с приглашением")
    public void friendInvitationExists(){
        friendTable
                .$("[data-tooltip-id='submit-invitation']").shouldBe(visible);
    }

    @Step("Проверка наличия строки с другом")
    public void friendExists(){
        friendTable
                .$$("tr")
                .find(text("You are friends"))
                .$$("td")
                .shouldHave(sizeGreaterThan(0));
    }
}
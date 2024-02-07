package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {


    private final SelenideElement peopleTable = $(".abstract-table tbody");

    @Step("Проверка наличия запросом дружбы")
    public void pendingInvitationExists(){
        peopleTable
                .$$("tr")
                .find(text("Pending invitation"))
                .$$("td")
                .shouldHave(sizeGreaterThan(0));
    }


    public void personExistsInAllPeople(String userName) {
        peopleTable
                .$$("tr")
                .find(text(userName))
                .$$("td")
                .shouldHave(sizeGreaterThan(0));
    }
}
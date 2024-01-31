package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;;

public class AllPeoplePage {

    private final SelenideElement youAreFriends = $(byText("Pending invitation"));

    @Step("Проверить что таблица содержит текст Pending invitation")
    public AllPeoplePage checkTextPendingInvitationVisible() {
        youAreFriends.shouldBe(Condition.visible);
        return this;
    }
}

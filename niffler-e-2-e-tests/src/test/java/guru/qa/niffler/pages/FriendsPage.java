package guru.qa.niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {

    private final SelenideElement submitInvitation = $x("//*[@data-tooltip-id='submit-invitation']//button[contains(@class,'button-icon_type_submit')]");
    private final SelenideElement declineInvitation = $x("//*[@data-tooltip-id='decline-invitation']//button[contains(@class,'button-icon_type_close')]");
    private final SelenideElement friendsTable = $x("//tbody//td");
    private final SelenideElement youAreFriends = $(byText("You are friends"));
    private final SelenideElement removeFriend = $x("//*[@data-tooltip-id='remove-friend']//button");
    private final SelenideElement avatarFriends = $x("//td//img");

    @Step("Проверить что таблица содержит текст You are friends")
    public FriendsPage checkFriendsTextVisible() {
        youAreFriends.shouldBe(Condition.visible);
        return this;
    }

    @Step("Проверить что таблица содержит кнопку Remove friend")
    public FriendsPage checkRemoveFriendButtonVisible() {
        removeFriend.shouldBe(Condition.visible);
        return this;
    }

    @Step("Проверить что таблица содержит аватар друга")
    public FriendsPage checkAvatarVisible() {
        avatarFriends.shouldBe(Condition.visible);
        return this;
    }

    @Step("Проверить что таблица не содержит записей")
    public FriendsPage checkTableFriendIsEmpty() {
        friendsTable.$$("tr").shouldHave(size(0));
        return this;
    }

    @Step("Проверить отображение кнопки Submit invitation")
    public FriendsPage checkSubmitInvitationVisible() {
        submitInvitation.shouldBe(Condition.visible);
        return this;
    }

    @Step("Проверить отображение кнопки Decline invitation")
    public FriendsPage checkDeclineInvitationVisible() {
        declineInvitation.shouldBe(Condition.visible);
        return this;
    }
}

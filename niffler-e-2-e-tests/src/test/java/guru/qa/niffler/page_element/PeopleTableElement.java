package guru.qa.niffler.page_element;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PeopleTableElement {
    private final SelenideElement
            peopleTable = $(".people-content tbody");

    private final String
            addFriendButton = "[data-tooltip-id='add-friend']",
            submitInvitationButton = "[data-tooltip-id='submit-invitation']",
            declineInvitationButton = "[data-tooltip-id='decline-invitation']",
            removeFriendButton = "[data-tooltip-id='remove-friend']";


    public PeopleTableElement verifyFriendsListNotEmpty() {
        peopleTable.$$("tr")
                .find(text("You are friends"))
                .shouldBe(visible);

        return this;
    }

    public PeopleTableElement verifyReceivedFriendsInvitation() {
        peopleTable.$("tr")
                .$(submitInvitationButton)
                .shouldBe(visible);

        return this;
    }

    public PeopleTableElement verifyPendingFriendsInvitation() {
        peopleTable.$$("tr")
                .find(text("Pending invitation"))
                .shouldBe(visible);

        return this;
    }

    public PeopleTableElement verifyExistingButtonRemoveFriend() {
        peopleTable.$("tr")
                .$(removeFriendButton)
                .shouldBe(visible);

        return this;
    }

    public PeopleTableElement verifyPendingInvitationToUser(String username) {
        peopleTable.$$("tr")
                .get(2)
                .$$("td")
                .get(1)
                .shouldHave(text(username));

        return this;
    }

    public PeopleTableElement verifyExistingDeclineInvitationBtn() {
        peopleTable.$$("tr")
                .get(2)
                .$(declineInvitationButton)
                .shouldBe(visible);

        return this;
    }
}

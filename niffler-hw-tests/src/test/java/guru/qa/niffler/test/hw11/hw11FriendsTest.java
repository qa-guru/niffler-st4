package guru.qa.niffler.test.hw11;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.pages.message.SuccessMsg;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;

@Execution(ExecutionMode.SAME_THREAD)
public class hw11FriendsTest extends BaseWebTest {

    private final static String tester1 = "tester1";
    private final static String tester2 = "tester2";

    @BeforeEach
    public void beforeEach(){
        Selenide.open("http://127.0.0.1:3000/main");
        welcomePage.clickLoginLink();
        loginPage.login(tester1, "12345");
    }

    @Test
    void inviteFriend() throws IOException {

        friendsApiClient.removeFriend(tester1, tester2);

        topMenu.clickPeopleTopMenu();

        peoplePage.clickAddFriend(tester2);

        profilePage.checkMessage(SuccessMsg.INVITATION_SENT_MSG);

        // Cleanup
        friendsApiClient.declineInvitation(tester1, new FriendJson(tester2));

    }

    @Test
    void submitInvitation() throws IOException {

        FriendJson friend1 = new FriendJson(tester1);
        FriendJson friend2 = new FriendJson(tester2);
        friendsApiClient.removeFriend(tester1, tester2);
        friendsApiClient.addFriend(tester2, friend1);

        topMenu.clickFriendsTopMenu();

        friendsPage.submitInvitation(tester2);

        profilePage.checkMessage(SuccessMsg.INVITATION_ACCEPTED_MSG);

        // Cleanup
        friendsApiClient.removeFriend(tester1, tester2);

    }

    @Test
    void declineInvitation() throws IOException {

        FriendJson friend1 = new FriendJson(tester1);
        FriendJson friend2 = new FriendJson(tester2);
        friendsApiClient.removeFriend(tester1, tester2);
        friendsApiClient.addFriend(tester2, friend1);

        topMenu.clickFriendsTopMenu();

        friendsPage.declineInvitation(tester2);

        profilePage.checkMessage(SuccessMsg.INVITATION_DECLINED_MSG);

    }

    @Test
    void removeFriend() throws IOException {

        FriendJson friend1 = new FriendJson(tester1);
        FriendJson friend2 = new FriendJson(tester2);
        friendsApiClient.addFriend(tester1, friend2);
        friendsApiClient.acceptInvitation(tester2, friend1);

        topMenu.clickFriendsTopMenu();

        friendsPage.removeFriend(tester2);

        profilePage.checkMessage(SuccessMsg.FRIEND_DELETED_MSG);

    }

}

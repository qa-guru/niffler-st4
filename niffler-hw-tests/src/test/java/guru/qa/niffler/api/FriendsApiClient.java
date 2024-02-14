package guru.qa.niffler.api;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class FriendsApiClient extends RestClient {

    private final FriendsApi friendsApi;

    public FriendsApiClient() {
        super("http://127.0.0.1:8089");
        this.friendsApi = retrofit.create(FriendsApi.class);
    }

    @Step("getFriends: {username}")
    public List<UserJson> getFriends(String username, boolean includePending) throws IOException {
        return friendsApi.getFriends(username, includePending).execute().body();
    }

    @Step("getInvitations: {username}")
    public List<UserJson> getInvitations(String username) throws IOException {
        return friendsApi.getInvitations(username).execute().body();
    }

    @Step("acceptInvitation: {username}")
    public List<UserJson> acceptInvitation(String username, FriendJson invitation) throws IOException {
        return friendsApi.acceptInvitation(username, invitation).execute().body();
    }

    @Step("declineInvitation: {username}")
    public List<UserJson> declineInvitation(String username, FriendJson invitation) throws IOException {
        return friendsApi.declineInvitation(username, invitation).execute().body();
    }

    @Step("addFriend: {username}")
    public UserJson addFriend(String username, FriendJson friend) throws IOException {
        return friendsApi.addFriend(username, friend).execute().body();
    }

    @Step("removeFriend: {username} {friendUsername}")
    public List<UserJson> removeFriend(String username, String friendUsername) throws IOException {
        return friendsApi.removeFriend(username, friendUsername).execute().body();
    }

}

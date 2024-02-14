package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class UserApiClient extends RestClient {

    private final UserApi userApi;

    public UserApiClient() {
        super("http://127.0.0.1:8089");
        this.userApi = retrofit.create(UserApi.class);
    }

    @Step("updateUserInfo")
    public UserJson updateUserInfo(UserJson user) throws IOException {
        return userApi.updateUserInfo(user).execute().body();
    }

    @Step("getCurrentUser: {username}")
    public UserJson getCurrentUser(String username) throws IOException {
        return userApi.getCurrentUser(username).execute().body();
    }

    @Step("getAllUsers: {username}")
    public List<UserJson> getAllUsers(String username) throws IOException {
        return userApi.getAllUsers(username).execute().body();
    }

}

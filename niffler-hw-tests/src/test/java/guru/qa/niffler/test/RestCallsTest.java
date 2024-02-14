package guru.qa.niffler.test;

import guru.qa.niffler.api.*;
import guru.qa.niffler.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

public class RestCallsTest extends BaseWebTest {

    SpendApiClient spendApiClient = new SpendApiClient();
    CategoryApiClient categoryApiClient = new CategoryApiClient();
    CurrencyApiClient currencyApiClient = new CurrencyApiClient();
    UserApiClient userApiClient = new UserApiClient();
    FriendsApiClient friendsApiClient = new FriendsApiClient();

    String username = "duck";

    @Test
    void spendApiCalls() throws IOException {

        SpendJson spendJson = new SpendJson(
                null, new Date(), "Обучение", CurrencyValues.USD, 100d, "Оплата", username
        );

        var createdSpend = spendApiClient.addSpend(spendJson);

        //null
        var getSpends = spendApiClient.getSpends(username, null, new Date(2000, 01, 01), new Date());

        SpendJson spendJsonUpd = new SpendJson(
                createdSpend.id(), new Date(), "Обучение", CurrencyValues.USD, 100d, "Оплата UPD", username
        );

        SpendJson editSpend = spendApiClient.editSpend(spendJsonUpd);

        spendApiClient.deleteSpends(username, editSpend.id().toString());

        //null
        var getStatistic = spendApiClient.getStatistic(username, CurrencyValues.USD, CurrencyValues.USD, new Date(2000, 01, 01), new Date());

    }

    @Test
    void categoryApiCalls() throws IOException {

        CategoryJson categoryJson = new CategoryJson(null, "apiCategory", username);

        var createdCategory = categoryApiClient.addCategory(categoryJson);

        var categories = categoryApiClient.getCategories(username);
    }

    @Test
    void currencyApiCalls() throws IOException {

        CurrencyCalculateJson currencyCalculateJson = new CurrencyCalculateJson(
                CurrencyValues.USD, CurrencyValues.RUB, 15d, 1000d);

        var getAllCurrencies = currencyApiClient.getAllCurrencies();

        //null
        var calculate = currencyApiClient.calculate(currencyCalculateJson);
    }

    @Test
    void userApiCalls() throws IOException {

        var getCurrentUser = userApiClient.getCurrentUser(username);

        UserJson userJson = new UserJson(
                getCurrentUser.id(), getCurrentUser.username(), "Donald", "Ducker",
                getCurrentUser.currency(), getCurrentUser.photo(), getCurrentUser.friendState(), getCurrentUser.testData());

        var updateUserInfo = userApiClient.updateUserInfo(userJson);

        var getAllUsers = userApiClient.getAllUsers(username);

    }

    @Test
    void friendsApiCalls() throws IOException {

        String user1 = "albert.stamm";
        String user2 = "valeria.boehm";

        FriendJson friend1 = new FriendJson(user1);
        FriendJson friend2 = new FriendJson(user2);

        friendsApiClient.addFriend(user1, friend2);

        var getInvitations = friendsApiClient.getInvitations(user2);

        friendsApiClient.acceptInvitation(user2, friend1);

        friendsApiClient.removeFriend(user1, user2);

        friendsApiClient.addFriend(user1, friend2);
        friendsApiClient.declineInvitation(user2, friend1);

    }
}

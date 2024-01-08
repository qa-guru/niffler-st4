package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import guru.qa.niffler.jupiter.User;
import java.util.UUID;

public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photo")
    String photo,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("friendState")
    FriendState friendState,
    @JsonIgnore
    TestData testData
) {

    public static UserJson createUser(String username,  String password, User.UserType userType,
        String friendName) {
        return new UserJson(
            null,
            username,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            new TestData(
                password,
                friendName
            )
        );
    }
}

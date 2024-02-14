package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserApi {

    @POST("/updateUserInfo")
    @Headers("Content-Type: application/json")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @GET("/currentUser")
    Call<UserJson> getCurrentUser(@Query("username") String username);

    @GET("/allUsers")
    Call<List<UserJson>> getAllUsers(@Query("username") String username);
}

package guru.qa.niffler.api;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FriendsApi {

    @GET("/friends")
    Call<List<UserJson>> getFriends(@Query("username") String username,
                                    @Query("includePending") boolean includePending);

    @GET("/invitations")
    Call<List<UserJson>> getInvitations(@Query("username") String username);

    @POST("/acceptInvitation")
    @Headers("Content-Type: application/json")
    Call<List<UserJson>> acceptInvitation(@Query("username") String username,
                                          @Body FriendJson invitation);

    @POST("/declineInvitation")
    @Headers("Content-Type: application/json")
    Call<List<UserJson>> declineInvitation(@Query("username") String username,
                                           @Body FriendJson invitation);

    @POST("/addFriend")
    @Headers("Content-Type: application/json")
    Call<UserJson> addFriend(@Query("username") String username,
                             @Body FriendJson friend);

    @DELETE("/removeFriend")
    Call<List<UserJson>> removeFriend(@Query("username") String username,
                                      @Query("friendUsername") String friendUsername);

}

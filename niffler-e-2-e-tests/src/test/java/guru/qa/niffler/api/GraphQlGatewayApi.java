package guru.qa.niffler.api;

import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.GqlUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GraphQlGatewayApi {

  @POST("/graphql")
  Call<GqlUser> currentUser(@Header("Authorization") String bearerToken,
                            @Body GqlRequest gqlRequest);

  @POST("/graphql")
  Call<Void> getFriends(@Header("Authorization") String bearerToken,
                        @Body GqlRequest gqlRequest);

  @POST("/graphql")
  Call<Void> users(@Header("Authorization") String bearerToken,
                   @Body GqlRequest gqlRequest);

  @POST("/graphql")
  Call<Void> updateUser(@Header("Authorization") String bearerToken,
                        @Body GqlRequest gqlRequest);
}

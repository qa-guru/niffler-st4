package guru.qa.niffler.api;

import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SoapUserdataApi {

  @POST("/ws")
  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  Call<CurrentUserResponse> currentUser(@Body CurrentUserRequest request);

}

package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJsonModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendApi {

  @POST("/addSpend")
  Call<SpendJsonModel> addSpend(@Body SpendJsonModel spend);
}

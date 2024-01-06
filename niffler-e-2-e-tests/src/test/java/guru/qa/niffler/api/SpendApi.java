package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface SpendApi {

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);
}

package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyCalculateJson;
import guru.qa.niffler.model.CurrencyJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.*;
import retrofit2.http.Headers;

import java.util.List;

public interface CurrencyApi {

    @GET("/getAllCurrencies")
    Call<List<CurrencyJson>> getAllCurrencies();

    @POST("/calculate")
    @Headers("Content-Type: application/json")
    Call<CurrencyCalculateJson> calculate(@Body CurrencyCalculateJson currencyCalculate);

}

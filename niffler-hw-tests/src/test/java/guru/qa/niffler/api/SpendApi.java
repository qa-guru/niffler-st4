package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface SpendApi {

    @GET("/spends")
    Call<List<SpendJson>> getSpends(@Query("username") String username,
                                    @Query("filterCurrency") CurrencyValues filterCurrency,
                                    @Query("from") Date from,
                                    @Query("to") Date to);

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @PATCH("/editSpend")
    @Headers("Content-Type: application/json")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("/deleteSpends")
    Call<Void> deleteSpends(@Query("username") String username,
                            @Query("ids") List<String> ids);

    @GET("/statistic")
    Call<List<StatisticJson>> getStatistic(@Query("username") String username,
                                           @Query("userCurrency") CurrencyValues userCurrency,
                                           @Query("filterCurrency") CurrencyValues filterCurrency,
                                           @Query("from") Date from,
                                           @Query("to") Date to);

}

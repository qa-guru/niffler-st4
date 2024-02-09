package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super("http://127.0.0.1:8093");
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("getSpends: {username}")
    public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency, Date from, Date to) throws IOException {
        return spendApi.getSpends(username, filterCurrency, from, to).execute().body();
    }

    @Step("Add spend '{spend.description}'")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend).execute().body();
    }

    @Step("editSpend")
    public SpendJson editSpend(SpendJson spend) throws IOException {
        return spendApi.editSpend(spend).execute().body();
    }

    @Step("deleteSpends: {username}")
    public void deleteSpends(String username, String... ids) throws IOException {
        spendApi.deleteSpends(username, Arrays.asList(ids)).execute().body();
    }

    @Step("getStatistic: {username}")
    public List<StatisticJson> getStatistic(String username, CurrencyValues userCurrency, CurrencyValues filterCurrency, Date from, Date to) throws IOException {
        return spendApi.getStatistic(username, userCurrency, filterCurrency, from, to).execute().body();
    }
}

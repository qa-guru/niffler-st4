package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super("http://127.0.0.1:8093");
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("Add spend '{spend.description}'")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend).execute().body();
    }
}

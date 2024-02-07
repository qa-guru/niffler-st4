package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;

import java.io.IOException;

public class GhApiClient extends RestClient {

    private final GhApi ghApi;

    public GhApiClient() {
        super("https://api.github.com");
        this.ghApi = retrofit.create(GhApi.class);
    }

    @Step("Get issue state by number: {issueNumber}")
    public String getIssueState(String issueNumber) throws IOException {
        String ghToken = System.getenv("GH_TOKEN");
        if (ghToken.isEmpty()){
            throw new RuntimeException("Github token is empty, please set env variable.");
        }
        JsonNode responseBody = ghApi.issue(
                "Bearer " + ghToken,
                issueNumber
        ).execute().body();
        return responseBody.get("state").asText();
    }
}

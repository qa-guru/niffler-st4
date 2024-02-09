package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class CategoryApiClient extends RestClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super("http://127.0.0.1:8093");
        this.categoryApi = retrofit.create(CategoryApi.class);
    }

    @Step("getCategories: {username}")
    public List<CategoryJson> getCategories(String username) throws IOException {
        return categoryApi.getCategories(username).execute().body();
    }

    @Step("addCategory")
    public CategoryJson addCategory(CategoryJson categoryJson) throws IOException {
        return categoryApi.addCategory(categoryJson).execute().body();
    }
}

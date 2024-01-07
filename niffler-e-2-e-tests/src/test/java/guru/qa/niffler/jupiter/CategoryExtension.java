package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryExtension.class);

    public static final String MAP_KEY = "category";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoryApi categoryApi = RETROFIT.create(CategoryApi.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(MAP_KEY, CategoryJson.class);
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws IOException {
        Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (category.isPresent()) {
            GenerateCategory categoryDate = category.get();
            CategoryJson categoryJson = new CategoryJson(null, categoryDate.category(), categoryDate.username());
            List<CategoryJson> categoriesUser = categoryApi.getCategories(categoryDate.username()).execute().body();
            if (!categoriesUser.stream().anyMatch(x -> x.category().equals(categoryDate.category()))) {
                CategoryJson created = categoryApi.addCategory(categoryJson).execute().body();
                extensionContext.getStore(NAMESPACE)
                        .put(MAP_KEY, created);
            }
            extensionContext.getStore(NAMESPACE)
                    .put(MAP_KEY, new CategoryJson(null, categoryDate.category(), categoryDate.username()));
        }
    }
}

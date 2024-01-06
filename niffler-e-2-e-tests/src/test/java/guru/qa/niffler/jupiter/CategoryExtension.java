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

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoryApi categoryApi = RETROFIT.create(CategoryApi.class);

    @Override
    public void beforeEach(ExtensionContext context) throws IOException {
        Optional<GenerateCategory> categoryAnnotation = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (categoryAnnotation.isPresent()) {
            GenerateCategory categoryData = categoryAnnotation.get();
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryData.category(),
                    categoryData.username()
            );

            boolean isNotExistCategoryInDB = false;
            List<CategoryJson> existingCategoriesInDB = categoryApi.getCategories(categoryData.username()).execute().body();
            if (existingCategoriesInDB != null) {
                isNotExistCategoryInDB = existingCategoriesInDB.stream().noneMatch(category -> category.category().equals(categoryJson.category()));
                context.getStore(NAMESPACE)
                        .put("category", existingCategoriesInDB.stream()
                                .filter(c -> c.category().equals(categoryJson.category()))
                                .findAny());
            }

            if (isNotExistCategoryInDB) {
                CategoryJson createdCategory = categoryApi.addCategory(categoryJson).execute().body();
                context.getStore(NAMESPACE)
                        .put("category", createdCategory);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(CategoryExtension.NAMESPACE)
                .get("category", CategoryJson.class);
    }
}

package guru.qa.niffler.jupiter;

import java.util.List;
import java.util.Optional;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.client.HttpClient.retrofit;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE_CATEGORY
            = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);


    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (category.isPresent()) {
            GenerateCategory categoryData = category.get();
            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryData.description(),
                    categoryData.username()
            );

            List<CategoryJson> categoryJsonList = categoryApi.getCategories(categoryData.username()).execute().body();

            if (categoryJsonList != null) {
                if (checkCategoryNotExistForUser(categoryJsonList, categoryJson.username())) {
                    CategoryJson createdCategory = categoryApi.addCategory(categoryJson).execute().body();
                    extensionContext.getStore(NAMESPACE_CATEGORY)
                            .put("category", createdCategory);
                } else {
                    CategoryJson existingCategory = categoryJsonList.stream()
                            .filter(categoryJson1 -> categoryJson.category().equals(categoryData.description()))
                            .findFirst()
                            .get();
                    extensionContext.getStore(NAMESPACE_CATEGORY)
                            .put("category", existingCategory);
                }
            }
        }
    }

    @Override public boolean supportsParameter(ParameterContext parameterContext,
                                               ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override public Object resolveParameter(ParameterContext parameterContext,
                                             ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE_CATEGORY)
                .get("spend", CategoryJson.class);
    }

    private boolean checkCategoryNotExistForUser(List<CategoryJson> categoryJsonList, String username) {
        return categoryJsonList.stream()
                .noneMatch(categoryJson -> categoryJson.category().equals(username));
    }
}

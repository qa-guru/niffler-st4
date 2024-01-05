package guru.qa.niffler.jupiter;

import java.util.List;
import java.util.Optional;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.client.HttpClient.retrofit;

public class DeleteSpendingsIfExist implements BeforeEachCallback {

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<DeleteUserSpendingsIfExist> userAnnotation = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                DeleteUserSpendingsIfExist.class);

        if (userAnnotation.isPresent()) {
            DeleteUserSpendingsIfExist user = userAnnotation.get();

            List<SpendJson> spendsToDelete = spendApi.getSpends(user.username()).execute().body();

            if (!spendsToDelete.isEmpty()) {
                List<String> idsToDelete = spendsToDelete.stream()
                        .map(spendJson -> spendJson.id().toString())
                        .toList();
                spendApi.deleteSpends(user.username(), idsToDelete).execute();
            }
        }

    }
}

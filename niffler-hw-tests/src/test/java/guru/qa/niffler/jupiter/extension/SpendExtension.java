package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.Optional;

public class SpendExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(SpendExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        GenerateSpend.class
    );

    if (spend.isPresent()) {
      GenerateSpend spendData = spend.get();
      SpendJson spendJson = new SpendJson(
          null,
          new Date(),
          spendData.category(),
          spendData.currency(),
          spendData.amount(),
          spendData.description(),
          spendData.username()
      );

      SpendJson created = spendApiClient.addSpend(spendJson);

      extensionContext.getStore(NAMESPACE)
          .put(extensionContext.getUniqueId(), created);
    }
  }
}

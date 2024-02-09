package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public abstract class SpendExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE
          = ExtensionContext.Namespace.create(SpendExtension.class);

  abstract SpendJson create(SpendJson spend) throws IOException;

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

      SpendJson created = create(spendJson);

      extensionContext.getStore(NAMESPACE)
              .put(extensionContext.getUniqueId(), created);
    }
  }
}

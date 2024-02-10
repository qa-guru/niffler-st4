package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {

  @Override
  default void beforeAll(ExtensionContext extensionContext) throws Exception {
    extensionContext.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
        .getOrComputeIfAbsent(this.getClass(), key -> {

          beforeSuite(extensionContext);

          return new ExtensionContext.Store.CloseableResource() {
            @Override
            public void close() throws Throwable {
              afterSuite();
            }
          };
        });
  }

  default void beforeSuite(ExtensionContext extensionContext) {

  }

  default void afterSuite() {

  }
}

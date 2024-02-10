package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.EmfProvider;
import jakarta.persistence.EntityManagerFactory;

public class EmfExtension implements SuiteExtension {

  @Override
  public void afterSuite() {
    EmfProvider.INSTANCE.storedEmf().forEach(
        EntityManagerFactory::close
    );
  }
}

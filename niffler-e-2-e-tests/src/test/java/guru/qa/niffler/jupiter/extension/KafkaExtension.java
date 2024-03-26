package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.kafka.KafkaService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaExtension implements SuiteExtension {

  private static final KafkaService ks = new KafkaService();
  private static final ExecutorService executor = Executors.newSingleThreadExecutor();


  @Override
  public void beforeSuite(ExtensionContext extensionContext) {
    executor.execute(ks);
    executor.shutdown();
  }

  @Override
  public void afterSuite() {
    ks.stop();
  }
}

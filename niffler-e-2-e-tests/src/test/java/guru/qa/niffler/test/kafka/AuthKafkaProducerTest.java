package guru.qa.niffler.test.kafka;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.kafka.KafkaService;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthKafkaProducerTest extends BaseKafkaTest {

  private final AuthApiClient authApiClient = new AuthApiClient();

  @Test
  void messageShouldBeProducedToKafkaAfterSuccessfulRegistration() throws Exception {
    String username = DataUtils.generateRandomUsername();
    String password = "12345";
    authApiClient.register(username, password);

    UserJson userFromKafka = KafkaService.getMessage(username);

    Assertions.assertEquals(
        username,
        userFromKafka.username()
    );
  }
}

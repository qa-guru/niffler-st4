package guru.qa.niffler.test.grphql;

import guru.qa.niffler.api.GatewayGqlApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GqlTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import org.junit.jupiter.api.extension.RegisterExtension;


@GqlTest
public abstract class BaseGraphQLTest {

  @RegisterExtension
  protected final ApiLoginExtension apiLoginExtension = new ApiLoginExtension(false);

  protected static final Config CFG = Config.getInstance();

  protected final GatewayGqlApiClient gatewayGqlApiClient = new GatewayGqlApiClient();

}

package guru.qa.niffler.test.soap;

import guru.qa.niffler.api.UserdataSoapClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;

@SoapTest
public abstract class BaseSoapTest {

  protected static final Config CFG = Config.getInstance();

  protected final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();
}

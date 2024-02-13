package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

  protected static final Config CFG = Config.getInstance();

}

package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.message.Msg;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage> {

  protected static final Config CFG = Config.getInstance();

  protected final SelenideElement toaster = $(".Toastify__toast-body");

  public abstract T waitForPageLoaded();

  @Step("Check that success message appears: {msg}")
  @SuppressWarnings("unchecked")
  public T checkToasterMessage(Msg msg) {
    toaster.should(Condition.visible).should(text(msg.getMessage()));
    return (T) this;
  }
}

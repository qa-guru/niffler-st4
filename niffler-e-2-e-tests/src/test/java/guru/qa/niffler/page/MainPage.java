package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.PhotoCondition.photoFromClasspath;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement avatar = $(".header__avatar");
  private final SelenideElement statistics = $(".main-content__section.main-content__section-stats");
  private final SpendingTable spendingTable = new SpendingTable();

  public MainPage checkThatStatisticDisplayed() {
    statistics.should(visible);
    return this;
  }

  @Step("check avatar")
  public MainPage checkAvatar(String imageName) {
    avatar.shouldHave(photoFromClasspath(imageName));
    return this;
  }

  public SpendingTable getSpendingTable() {
    return spendingTable;
  }
}

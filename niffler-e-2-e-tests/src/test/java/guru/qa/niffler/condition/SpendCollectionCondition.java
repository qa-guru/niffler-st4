package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.List;

public class SpendCollectionCondition {

  public static CollectionCondition spends(SpendJson... expectedSPends) {
    return new CollectionCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSPends.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }


        for (WebElement element : elements) {

          List<WebElement> tds = element.findElements(By.cssSelector("td"));
          boolean checkPassed = false;

          for (SpendJson expectedSPend : expectedSPends) {
            checkPassed = tds.get(4).getText().equals(expectedSPend.category());
            if (checkPassed) {
              break;
            }
          }

          if (checkPassed) {
            return CheckResult.accepted();
          } else {
            return CheckResult.rejected("Incorrect spends content", elements);
          }
        }


        return super.check(driver, elements);
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };
  }
}

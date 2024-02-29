package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.conditions.Not;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PhotoCondition {

  public static Condition photoFromClasspath(String expectedPhoto) {
    return new Condition("photo") {
      @Nonnull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        String imageAsBase64 = null;

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(expectedPhoto)) {
          imageAsBase64 = new String(Base64.getEncoder().encode(is.readAllBytes()), StandardCharsets.UTF_8);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }


        String src = StringUtils.substringAfter(element.getAttribute("src"), "base64,");
        boolean matched = src.equals(imageAsBase64);

        return new CheckResult(matched, matched ? "avatars are same" : "avatars are different");
      }

      @Nonnull
      @Override
      public Condition negate() {
        return new Not(this, true);
      }
    };
  }
}

package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static guru.qa.niffler.jupiter.annotation.User.Point.OUTER;

//@WireMockTest(httpPort = 8089)
public class LoginTest extends BaseWebTest {

  private static final WireMockServer wiremock = new WireMockServer(wireMockConfig()
      .port(8089));

  @BeforeAll
  static void start() {
    wiremock.start();
  }

  @AfterAll
  static void stop() {
    wiremock.stop();
  }

  @BeforeEach
  void configure() {
    new WireMock("http://userdata.nifller.dc", 8089).register(get(urlPathEqualTo("/currentUser"))
        .willReturn(
            okJson("{\n" +
                "      \"id\": \"229fc371-2821-4795-81a5-0b26d3cd417e\",\n" +
                "      \"username\": \"{{ request.query.username }}\",\n" +
                "      \"firstname\": null,\n" +
                "      \"surname\": null,\n" +
                "      \"currency\": \"RUB\",\n" +
                "      \"photo\": null\n" +
                "    }")
        ));
  }

  @TestUser()
  @Test
  void statisticShouldBeVisibleAfterLogin(@User(OUTER) UserJson user) {
    Selenide.open(WelcomePage.URL, WelcomePage.class)
        .doLogin()
        .fillLoginPage(user.username(), user.testData().password())
        .submit();

    new MainPage()
        .waitForPageLoaded();
  }
}

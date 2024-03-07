package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.cookie.ThreadSafeCookieManager;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.OauthUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.util.List;
import java.util.Map;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  private static final Config CFG = Config.getInstance();
  private final AuthApiClient authApiClient = new AuthApiClient();
  private final boolean initBrowser;

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);

  public ApiLoginExtension() {
    this(true);
  }

  public ApiLoginExtension(boolean initBrowser) {
    this.initBrowser = initBrowser;
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    ApiLogin apiLogin = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        ApiLogin.class
    ).orElse(null);

    final String login;
    final String password;

    if (apiLogin != null) {
      TestUser testUser = apiLogin.user();
      if (!testUser.fake()) {
        UserJson createdUserForApiLogin = getCreatedUserForApiLogin(extensionContext);
        login = createdUserForApiLogin.username();
        password = createdUserForApiLogin.testData().password();
      } else {
        login = apiLogin.username();
        password = apiLogin.password();
      }

      final String codeVerifier = OauthUtils.generateCodeVerifier();
      final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);
      setCodeVerifier(extensionContext, codeVerifier);
      setCodChallenge(extensionContext, codeChallenge);
      authApiClient.doLogin(extensionContext, login, password);

      if (initBrowser) {
        Selenide.open(CFG.frontUrl());
        SessionStorage sessionStorage = Selenide.sessionStorage();
        sessionStorage.setItem(
            "codeChallenge", getCodChallenge(extensionContext)
        );
        sessionStorage.setItem(
            "id_token", getToken(extensionContext)
        );
        sessionStorage.setItem(
            "codeVerifier", getCodeVerifier(extensionContext)
        );

        WebDriverRunner.getWebDriver().manage().addCookie(
            jsessionCookie()
        );
        Selenide.refresh();
      }
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.findAnnotation(parameterContext.getParameter(), Token.class).isPresent() &&
        parameterContext.getParameter().getType().isAssignableFrom(String.class);
  }

  @Override
  public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return "Bearer " + getToken(extensionContext);
  }

  @SuppressWarnings("unchecked")
  private static UserJson getCreatedUserForApiLogin(ExtensionContext extensionContext) {
    return ((List<UserJson>) extensionContext.getStore(CreateUserExtension.CREATE_USER_NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
        .get(User.Point.INNER))
        .getFirst();
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    ThreadSafeCookieManager.INSTANCE.removeAll();
  }

  public static void setCodeVerifier(ExtensionContext context, String codeVerifier) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("code_verifier", codeVerifier);
  }

  public static void setCodChallenge(ExtensionContext context, String codeChallenge) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("code_challenge", codeChallenge);
  }

  public static void setCode(ExtensionContext context, String code) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("code", code);
  }

  public static void setToken(ExtensionContext context, String token) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("token", token);
  }

  public static String getCodeVerifier(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("code_verifier", String.class);
  }

  public static String getCodChallenge(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("code_challenge", String.class);
  }

  public static String getCode(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("code", String.class);
  }

  public static String getToken(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("token", String.class);
  }

  public static String getCsrfToken() {
    return ThreadSafeCookieManager.INSTANCE.getCookieValue("XSRF-TOKEN");
  }

  public Cookie jsessionCookie() {
    return new Cookie(
        "JSESSIONID",
        ThreadSafeCookieManager.INSTANCE.getCookieValue("JSESSIONID")
    );
  }
}

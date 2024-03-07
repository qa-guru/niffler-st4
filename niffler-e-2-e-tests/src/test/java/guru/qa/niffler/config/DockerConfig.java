package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

  static final DockerConfig instance = new DockerConfig();

  private DockerConfig() {
  }

  static {
    Configuration.remote = "http://localhost:4444/wd/hub";
    Configuration.browser = "chrome";
  }

  @Override
  public String frontUrl() {
    return "http://frontend.niffler.dc";
  }

  @Override
  public String authUrl() {
    return "http://auth.niffler.dc:9000";
  }

  @Override
  public String gatewayUrl() {
    return "http://gateway.niffler.dc:8090";
  }

  @Override
  public String jdbcHost() {
    return "niffler-all-db";
  }

  @Override
  public String currencyGrpcHost() {
    return "currency.niffler.dc";
  }
}

package guru.qa.niffler.config;

public class DockerConfig implements Config {

  static final DockerConfig instance = new DockerConfig();

  private DockerConfig() {
  }

  @Override
  public String frontUrl() {
    return "http://frontend.niffler.dc";
  }

  @Override
  public String jdbcHost() {
    return "niffler-all-db";
  }
}

package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JdbcUrl {
  AUTH("jdbc:postgresql://%s:%d/niffler-auth"),
  CURRENCY("jdbc:postgresql://%s:%d/niffler-currency"),
  SPEND("jdbc:postgresql://%s:%d/niffler-spend"),
  USERDATA("jdbc:postgresql://%s:%d/niffler-userdata");

  private final String url;

  private static final Config cfg = Config.getInstance();

  public String getUrl() {
    return String.format(
        url,
        cfg.jdbcHost(),
        cfg.jdbcPort()
    );
  }
}

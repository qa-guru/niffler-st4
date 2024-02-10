package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DataSourceProvider {
  INSTANCE;

  private static final Config cfg = Config.getInstance();

  private final Map<Database, DataSource> store = new ConcurrentHashMap<>();

  public DataSource dataSource(Database database) {
    return store.computeIfAbsent(database, k -> {
      PGSimpleDataSource ds = new PGSimpleDataSource();
      ds.setURL(k.getUrl());
      ds.setUser(cfg.jdbcUser());
      ds.setPassword(cfg.jdbcPassword());
      return ds;
    });
  }
}

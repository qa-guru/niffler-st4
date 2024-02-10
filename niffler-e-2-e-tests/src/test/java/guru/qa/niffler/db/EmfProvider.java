package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.db.jpa.ThreadSafeEntityManagerFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EmfProvider {
  INSTANCE;

  private static final Config cfg = Config.getInstance();

  private final Map<Database, EntityManagerFactory> store = new ConcurrentHashMap<>();

  public EntityManagerFactory emf(Database database) {
    return store.computeIfAbsent(database, k -> {
      Map<String, String> settings = new HashMap<>();
      settings.put("hibernate.connection.url", k.getUrl());
      settings.put("hibernate.connection.user", cfg.jdbcUser());
      settings.put("hibernate.connection.password", cfg.jdbcPassword());
      settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
      settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
      return new ThreadSafeEntityManagerFactory(
          Persistence.createEntityManagerFactory("niffler-st4", settings)
      );
    });
  }

  public Collection<EntityManagerFactory> storedEmf() {
    return store.values();
  }
}

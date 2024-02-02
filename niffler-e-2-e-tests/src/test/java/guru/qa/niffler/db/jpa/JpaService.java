package guru.qa.niffler.db.jpa;

import guru.qa.niffler.db.Database;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class JpaService {

  private final Map<Database, EntityManager> emStore;

  public JpaService(Database database, EntityManager em) {
    this.emStore = new HashMap<>();
    this.emStore.put(database, em);
  }

  public JpaService(Map<Database, EntityManager> ems) {
    this.emStore = ems;
  }

  protected EntityManager entityManager(Database database) {
    return emStore.get(database);
  }

  protected <T> void persist(Database database, T entity) {
    tx(database, em -> em.persist(entity));
  }

  protected <T> void remove(Database database, T entity) {
    tx(database, em -> em.remove(entity));
  }

  protected <T> T merge(Database database, T entity) {
    return txWithResult(database, em -> em.merge(entity));
  }

  protected <T> T txWithResult(Database database, Function<EntityManager, T> function) {
    EntityTransaction transaction = entityManager(database).getTransaction();
    transaction.begin();
    try {
      T result = function.apply(entityManager(database));
      transaction.commit();
      return result;
    } catch (Exception e) {
      transaction.rollback();
      throw e;
    }
  }

  protected void tx(Database database, Consumer<EntityManager> consumer) {
    EntityTransaction transaction = entityManager(database).getTransaction();
    transaction.begin();
    try {
      consumer.accept(entityManager(database));
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      throw e;
    }
  }
}

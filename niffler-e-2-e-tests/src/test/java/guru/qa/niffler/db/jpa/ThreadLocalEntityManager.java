package guru.qa.niffler.db.jpa;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ThreadLocalEntityManager implements EntityManager {

    private final ThreadLocal<EntityManager> emtl = new ThreadLocal<>();
    private final EntityManagerFactory emf;

    public ThreadLocalEntityManager(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager threadLocalEm() {
        if (emtl.get() == null) {
            emtl.set(emf.createEntityManager());
        }
        return emtl.get();
    }

    @Override
    public void persist(Object entity) {
        threadLocalEm().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return threadLocalEm().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        threadLocalEm().remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return threadLocalEm().find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return threadLocalEm().find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return threadLocalEm().find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return threadLocalEm().find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return threadLocalEm().getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        threadLocalEm().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        threadLocalEm().setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return threadLocalEm().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        threadLocalEm().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        threadLocalEm().lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(Object entity) {
        threadLocalEm().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        threadLocalEm().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        threadLocalEm().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        threadLocalEm().refresh(entity, lockMode, properties);
    }

    @Override
    public void clear() {
        threadLocalEm().clear();
    }

    @Override
    public void detach(Object entity) {
        threadLocalEm().detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return threadLocalEm().contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return threadLocalEm().getLockMode(entity);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        threadLocalEm().setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return threadLocalEm().getProperties();
    }

    @Override
    public Query createQuery(String qlString) {
        return threadLocalEm().createQuery(qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return threadLocalEm().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return threadLocalEm().createQuery(updateQuery);
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return threadLocalEm().createQuery(deleteQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return threadLocalEm().createQuery(qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(String name) {
        return threadLocalEm().createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return threadLocalEm().createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return threadLocalEm().createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return threadLocalEm().createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return threadLocalEm().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return threadLocalEm().createNamedStoredProcedureQuery(name);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return threadLocalEm().createStoredProcedureQuery(procedureName);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return threadLocalEm().createStoredProcedureQuery(procedureName, resultClasses);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return threadLocalEm().createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    @Override
    public void joinTransaction() {
        threadLocalEm().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return threadLocalEm().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return threadLocalEm().unwrap(cls);
    }

    @Override
    public Object getDelegate() {
        return threadLocalEm().getDelegate();
    }

    @Override
    public void close() {
        if (emtl.get() != null) {
            emtl.get().close();
        }
        emtl.remove();
    }

    @Override
    public boolean isOpen() {
        return threadLocalEm().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return threadLocalEm().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return threadLocalEm().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return threadLocalEm().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return threadLocalEm().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return threadLocalEm().createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return threadLocalEm().createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return threadLocalEm().getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return threadLocalEm().getEntityGraphs(entityClass);
    }
}

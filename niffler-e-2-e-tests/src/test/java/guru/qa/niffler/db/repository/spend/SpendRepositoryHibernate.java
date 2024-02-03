package guru.qa.niffler.db.repository.spend;

import static guru.qa.niffler.db.Database.SPEND;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.db.model.spend.SpendEntity;
import jakarta.persistence.NoResultException;
import java.util.Optional;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository {

  public SpendRepositoryHibernate() {
    super(SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager());
  }

  @Override
  public SpendEntity createSpend(SpendEntity spendEntity) {
    persist(SPEND, spendEntity);
    return spendEntity;
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity categoryEntity) {
    persist(SPEND, categoryEntity);
    return categoryEntity;
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUserIdAndCategory(String username, String category) {
    try {
      return Optional.of( entityManager(SPEND)
          .createQuery("""
            SELECT cat 
            FROM CategoryEntity cat 
            WHERE cat.category = :category
            AND cat.username = :username
            """, CategoryEntity.class)
          .setParameter("category", category)
          .setParameter("username", username)
          .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}

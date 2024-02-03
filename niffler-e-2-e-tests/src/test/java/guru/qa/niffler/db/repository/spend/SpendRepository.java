package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.db.model.spend.SpendEntity;
import java.util.Optional;

public interface SpendRepository {

  SpendEntity createSpend(SpendEntity spendEntity);

  CategoryEntity createCategory(CategoryEntity categoryEntity);

  Optional<CategoryEntity> findCategoryByUserIdAndCategory(String username, String category);
}

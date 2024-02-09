package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositoryHibernate;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.SpendJson;

public class DatabaseSpendExtension extends SpendExtension {

    @Override
    SpendJson create(SpendJson spendJson) {

        SpendingRepository repository = new SpendingRepositoryHibernate();

        CategoryEntity categoryEntity = new CategoryEntity();
        SpendEntity spendEntity = new SpendEntity();

        categoryEntity.setUsername(spendJson.username());
        categoryEntity.setCategory(spendJson.category());

        spendEntity.setUsername(spendJson.username());
        spendEntity.setSpendDate(spendJson.spendDate());
        spendEntity.setCurrency(spendJson.currency());
        spendEntity.setAmount(spendJson.amount());
        spendEntity.setDescription(spendJson.description());
        spendEntity.setCategory(categoryEntity);

        SpendEntity created = repository.createSpending(spendEntity);

        return created.toJsonModel();
    }
}

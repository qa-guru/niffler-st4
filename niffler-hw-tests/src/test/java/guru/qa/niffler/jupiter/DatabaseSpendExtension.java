package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.SpendingRepository;
import guru.qa.niffler.db.repository.SpendingRepositoryHibernate;
import guru.qa.niffler.model.SpendJson;

public class DatabaseSpendExtension extends SpendExtension {

    private SpendingRepository repository = new SpendingRepositoryHibernate();

    @Override
    SpendJson create(SpendJson spendJson) {
        SpendEntity spendEntity;
        CategoryEntity categoryEntity;

        categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(spendJson.username());
        categoryEntity.setCategory(spendJson.category());

        spendEntity = new SpendEntity();
        spendEntity.setUsername(spendJson.username());
        spendEntity.setSpendDate(spendJson.spendDate());
        spendEntity.setCurrency(spendJson.currency());
        spendEntity.setAmount(spendJson.amount());
        spendEntity.setDescription(spendJson.description());
        spendEntity.setCategory(categoryEntity);

        SpendEntity created = repository.createSpending(spendEntity);

        return new SpendJson(created.getId(), created.getSpendDate(), created.getCategory().toString(),
                created.getCurrency(), created.getAmount(), created.getDescription(), created.getUsername());
    }
}

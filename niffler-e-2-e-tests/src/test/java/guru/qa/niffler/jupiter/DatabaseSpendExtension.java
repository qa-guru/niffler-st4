package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.SpendRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.Optional;

public class DatabaseSpendExtension extends SpendExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DatabaseSpendExtension.class);

    @Override
    SpendJson create(SpendJson spend) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(spend.username());
        categoryEntity.setCategory(spend.category());

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(spend.username());
        spendEntity.setCategory(categoryEntity);
        spendEntity.setCurrency(spend.currency());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();
        SpendEntity spendEntityResult = spendRepositoryHibernate.createSpend(spendEntity);
        SpendJson spendJson = new SpendJson(
                spendEntityResult.getId(),
                spendEntityResult.getSpendDate(),
                spendEntityResult.getCategory().getCategory(),
                spendEntityResult.getCurrency(),
                spendEntityResult.getAmount(),
                spendEntityResult.getDescription(),
                spendEntityResult.getUsername());
        return spendJson;
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        );

        if (spend.isPresent()) {
            GenerateSpend spendData = spend.get();
            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    spendData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.username());

            extensionContext.getStore(NAMESPACE)
                    .put("spend", create(spendJson));
        }
    }
}

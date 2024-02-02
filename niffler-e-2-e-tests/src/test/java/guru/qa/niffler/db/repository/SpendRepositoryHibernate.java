package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Map;

import static guru.qa.niffler.db.Database.SPEND;

public class SpendRepositoryHibernate extends JpaService implements SpendRepository {
    public SpendRepositoryHibernate() {
        super( Map.of(
                SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager()
        ));
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        persist(SPEND, spend);
        return spend;
    }
}

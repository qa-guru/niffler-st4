package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Map;
import java.util.UUID;

import static guru.qa.niffler.db.Database.SPEND;

public class SpendingRepositoryHibernate extends JpaService implements SpendingRepository {

    public SpendingRepositoryHibernate() {
        super(
                Map.of(
                        SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager()
                )
        );
    }

    @Override
    public SpendEntity createSpending(SpendEntity spend) {

        spend.getCategory().setId(UUID.fromString(getCategoryById(spend)));

        persist(SPEND, spend);

        return spend;
    }

    private String getCategoryById(SpendEntity spend){
        return entityManager(SPEND).createQuery(
                        "SELECT c.id FROM CategoryEntity c WHERE c.category=:category AND c.username=:username"
                        , UUID.class)
                .setParameter("category", spend.getCategory().getCategory())
                .setParameter("username", spend.getUsername())
                .getSingleResult().toString();
    }
}

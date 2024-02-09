package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.SpendEntity;

public interface SpendingRepository {

    SpendEntity createSpending(SpendEntity spend);
}

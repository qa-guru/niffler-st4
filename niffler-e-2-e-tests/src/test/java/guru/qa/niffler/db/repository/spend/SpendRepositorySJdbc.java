package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.db.model.spend.SpendEntity;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;

public class SpendRepositorySJdbc implements SpendRepository {

  JdbcTemplate spendTemplate;

  public SpendRepositorySJdbc() {
    JdbcTransactionManager spendTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(Database.SPEND));
    this.spendTemplate = new JdbcTemplate(spendTm.getDataSource());
  }

  @Override
  public SpendEntity createSpend(SpendEntity spendEntity) {
    KeyHolder kh = new GeneratedKeyHolder();
    spendTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) "
              + "VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);

      ps.setString(1, spendEntity.getUsername());
      ps.setDate(2, new java.sql.Date(spendEntity.getSpendDate().getTime()));
      ps.setString(3, spendEntity.getCurrency().name());
      ps.setDouble(4, spendEntity.getAmount());
      ps.setString(5, spendEntity.getDescription());
      ps.setObject(6, spendEntity.getCategory().getId());

      return ps;
    }, kh);

    spendEntity.setId((UUID) kh.getKeys().get("id"));
    return spendEntity;
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity categoryEntity) {
    KeyHolder kh = new GeneratedKeyHolder();
    spendTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO category (category, username) VALUES (?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);

      ps.setString(1, categoryEntity.getCategory());
      ps.setString(2, categoryEntity.getUsername());

      return ps;
    }, kh);

    categoryEntity.setId((UUID) kh.getKeys().get("id"));
    return categoryEntity;
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUserIdAndCategory(String username,
      String category) {
    try {
      return Optional.ofNullable(
          spendTemplate.queryForObject("""
                  SELECT id,
                         category,
                         username
                  FROM category
                  WHERE  category = ?
                  AND username = ?
                  """,
              CategoryEntityRowMapper.instance,
              category,
              username));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}

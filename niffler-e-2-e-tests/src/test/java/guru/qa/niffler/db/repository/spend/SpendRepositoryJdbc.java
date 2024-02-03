package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.spend.CategoryEntity;
import guru.qa.niffler.db.model.spend.SpendEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

public class SpendRepositoryJdbc implements SpendRepository {

  DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);

  @Override
  public SpendEntity createSpend(SpendEntity spendEntity) {
    try (Connection conn = spendDs.getConnection()) {
      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) "
              + "VALUES (?,?,?,?,?,?)",
          PreparedStatement.RETURN_GENERATED_KEYS);

      ps.setString(1, spendEntity.getUsername());
      ps.setDate(2, new java.sql.Date(spendEntity.getSpendDate().getTime()));
      ps.setString(3, spendEntity.getCurrency().name());
      ps.setDouble(4, spendEntity.getAmount());
      ps.setString(5, spendEntity.getDescription());
      ps.setObject(6, spendEntity.getCategory().getId());

      ps.executeUpdate();

      UUID spendId;
      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) {
          spendId = UUID.fromString(keys.getString("id"));
        } else {
          throw new IllegalStateException("Can`t find id");
        }
      }
      spendEntity.setId(spendId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return spendEntity;
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity categoryEntity) {
    try (Connection conn = spendDs.getConnection()) {
      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO category (category, username) VALUES (?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      ps.setString(1, categoryEntity.getCategory());
      ps.setString(2, categoryEntity.getUsername());

      ps.executeUpdate();

      UUID categoryId;
      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) {
          categoryId = UUID.fromString(keys.getString("id"));
        } else {
          throw new IllegalStateException("Can`t find id");
        }
      }
      categoryEntity.setId(categoryId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categoryEntity;
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUserIdAndCategory(String username,
      String category) {
    try (Connection conn = spendDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement("""
          SELECT id,
                 category,
                 username
          FROM category
          WHERE  category = ?
          AND username = ?
          """);
      ) {
        ps.setString(1, category);
        ps.setString(2, username);
        ResultSet rs = ps.executeQuery();
        CategoryEntity categoryEntity = new CategoryEntity();
        if (rs.next()) {
          categoryEntity.setId(rs.getObject(1, UUID.class));
          categoryEntity.setCategory(rs.getString(2));
          categoryEntity.setUsername(rs.getString(3));
          return Optional.of(categoryEntity);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

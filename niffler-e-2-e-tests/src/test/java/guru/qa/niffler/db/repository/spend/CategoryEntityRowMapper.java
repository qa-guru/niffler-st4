package guru.qa.niffler.db.repository.spend;

import guru.qa.niffler.db.model.spend.CategoryEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

  public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

  @Override
  public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setId(rs.getObject(1, UUID.class));
    categoryEntity.setCategory(rs.getString(2));
    categoryEntity.setUsername(rs.getString(3));
    return categoryEntity;
  }

}

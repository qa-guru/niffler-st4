package guru.qa.niffler.db.sjdbc;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();

  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity user = new UserEntity();
    user.setId(rs.getObject("id", UUID.class));
    user.setUsername(rs.getString("username"));
    user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    user.setFirstname(rs.getString("firstname"));
    user.setSurname(rs.getString("surname"));
    user.setPhoto(rs.getBytes("photo"));
    return user;
  }
}

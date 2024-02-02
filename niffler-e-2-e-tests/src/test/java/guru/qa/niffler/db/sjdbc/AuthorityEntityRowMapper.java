package guru.qa.niffler.db.sjdbc;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

  public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

  @Override
  public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthorityEntity authorityEntity = new AuthorityEntity();
    authorityEntity.setId(rs.getObject(1, UUID.class));
    authorityEntity.setUserId(rs.getObject(2, UUID.class));
    authorityEntity.setAuthority(Enum.valueOf(Authority.class, rs.getString(3)));

    return authorityEntity;
  }
}

package guru.qa.niffler.db.sjdbc;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class UserAuthEntityResultSetExtractor implements ResultSetExtractor<UserAuthEntity> {

  public static final UserAuthEntityResultSetExtractor instance = new UserAuthEntityResultSetExtractor();

  @Override
  public UserAuthEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
    UserAuthEntity user = new UserAuthEntity();
    boolean userProcessed = false;
    while (rs.next()) {
      if (!userProcessed) {
        user.setId(rs.getObject(1, UUID.class));
        user.setUsername(rs.getString(2));
        user.setPassword(rs.getString(3));
        user.setEnabled(rs.getBoolean(4));
        user.setAccountNonExpired(rs.getBoolean(5));
        user.setAccountNonLocked(rs.getBoolean(6));
        user.setCredentialsNonExpired(rs.getBoolean(7));
        userProcessed = true;
      }

      AuthorityEntity authority = new AuthorityEntity();
      authority.setId(rs.getObject(8, UUID.class));
      authority.setAuthority(Authority.valueOf(rs.getString(10)));
      user.getAuthorities().add(authority);
    }
    return user;
  }
}

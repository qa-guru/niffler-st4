package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.sjdbc.UserAuthEntityResultSetExtractor;
import guru.qa.niffler.db.sjdbc.UserEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositorySJdbc implements UserRepository {

  private final TransactionTemplate authTxt;
  private final TransactionTemplate udTxt;
  private final JdbcTemplate authTemplate;
  private final JdbcTemplate udTemplate;

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserRepositorySJdbc() {
    JdbcTransactionManager authTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH)
    );
    JdbcTransactionManager udTm = new JdbcTransactionManager(
        DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA)
    );

    this.authTxt = new TransactionTemplate(authTm);
    this.udTxt = new TransactionTemplate(udTm);
    this.authTemplate = new JdbcTemplate(authTm.getDataSource());
    this.udTemplate = new JdbcTemplate(udTm.getDataSource());
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    return authTxt.execute(status -> {
      authTemplate.update(con -> {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO \"user\" " +
                "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, user.getUsername());
        ps.setString(2, pe.encode(user.getPassword()));
        ps.setBoolean(3, user.getEnabled());
        ps.setBoolean(4, user.getAccountNonExpired());
        ps.setBoolean(5, user.getAccountNonLocked());
        ps.setBoolean(6, user.getCredentialsNonExpired());
        return ps;
      }, kh);

      user.setId((UUID) kh.getKeys().get("id"));

      authTemplate.batchUpdate("INSERT INTO \"authority\" " +
          "(user_id, authority) " +
          "VALUES (?, ?)", new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
        }

        @Override
        public int getBatchSize() {
          return user.getAuthorities().size();
        }
      });

      return user;
    });
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    try {
      return Optional.ofNullable(
          authTemplate.query(
              "SELECT * " +
                  "FROM \"user\" u " +
                  "JOIN \"authority\" a ON u.id = a.user_id " +
                  "where u.id = ?",
              UserAuthEntityResultSetExtractor.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    udTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      return ps;
    }, kh);

    user.setId((UUID) kh.getKeys().get("id"));
    return user;
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    try {
      return Optional.ofNullable(
          udTemplate.queryForObject(
              "SELECT * FROM \"user\" WHERE id = ?",
              UserEntityRowMapper.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void deleteInAuthById(UUID id) {
    authTxt.execute(status -> {
      authTemplate.update("DELETE FROM \"authority\" WHERE user_id = ?", id);
      authTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    udTxt.execute(status -> {
      udTemplate.update("DELETE FROM friendship WHERE friend_id = ?", id);
      udTemplate.update("DELETE FROM friendship WHERE user_id = ?", id);
      udTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }
}

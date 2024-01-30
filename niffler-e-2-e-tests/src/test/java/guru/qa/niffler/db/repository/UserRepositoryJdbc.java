package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryJdbc implements UserRepository {

  private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH);
  private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA);

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
              +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
          PreparedStatement authorityPs = conn.prepareStatement(
              "INSERT INTO \"authority\" " +
                  "(user_id, authority) " +
                  "VALUES (?, ?)")
      ) {

        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());

        userPs.executeUpdate();

        UUID authUserId;
        try (ResultSet keys = userPs.getGeneratedKeys()) {
          if (keys.next()) {
            authUserId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }

        for (AuthorityEntity authority : user.getAuthorities()) {
          authorityPs.setObject(1, authUserId);
          authorityPs.setString(2, authority.getAuthority().name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        try (ResultSet authorityKeys = authorityPs.getGeneratedKeys()) {
          int i = 0;
          while (authorityKeys.next()) {
            user.getAuthorities().get(i).setId(UUID.fromString(authorityKeys.getString("id")));
            i++;
          }
        }
        conn.commit();
        user.setId(authUserId);
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, currency) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getCurrency().name());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        user.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public void deleteInAuthById(UUID id) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement authorityDeletePs = conn.prepareStatement("""
          DELETE FROM "authority" WHERE  user_id = ?
          """);
          PreparedStatement userDeletePs = conn.prepareStatement("""
              DELETE FROM "user" WHERE  id = ?
              """)
      ) {
        authorityDeletePs.setObject(1, id);
        authorityDeletePs.execute();

        userDeletePs.setObject(1, id);
        userDeletePs.execute();
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw new RuntimeException(e);
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    try (Connection conn = udDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement friendshipDeletePs = conn.prepareStatement("""
          DELETE FROM "friendship" WHERE user_id = ?
                        """);
          PreparedStatement userDeletePs = conn.prepareStatement("""
              DELETE FROM "user" WHERE id = ?
                  """)
      ) {
        friendshipDeletePs.setObject(1, id);
        friendshipDeletePs.execute();

        userDeletePs.setObject(1, id);
        userDeletePs.execute();
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw new RuntimeException(e);
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserAuthEntity> findUserInAuthById(UUID id) {
    try (Connection conn = authDs.getConnection()) {
      try (PreparedStatement userSelectPs = conn.prepareStatement("""
          SELECT u.id,
                 u.username,
                 u.password,
                 u.enabled,
                 u.account_non_expired,
                 u.account_non_locked,
                 u.credentials_non_expired,
                 ua.id,
                 ua.user_id,
                 ua.authority
          FROM "user" u
          INNER JOIN "authority" ua
                  ON ua.user_id = u.id
          WHERE  u.id = ?
          """);
      ) {
        userSelectPs.setObject(1, id);
        ResultSet rs = userSelectPs.executeQuery();
        boolean isUserProcessed = false;
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        while (rs.next()) {
          if (!isUserProcessed) {
            userAuthEntity.setId(rs.getObject(1, UUID.class));
            userAuthEntity.setUsername(rs.getString(2));
            userAuthEntity.setPassword(rs.getString(3));
            userAuthEntity.setEnabled(rs.getBoolean(4));
            userAuthEntity.setAccountNonExpired(rs.getBoolean(5));
            userAuthEntity.setAccountNonLocked(rs.getBoolean(6));
            userAuthEntity.setCredentialsNonExpired(rs.getBoolean(7));
            isUserProcessed = true;
          }

          AuthorityEntity authorityEntity = new AuthorityEntity();
          authorityEntity.setId(rs.getObject(8, UUID.class));
          authorityEntity.setUserId(rs.getObject(9, UUID.class));
          authorityEntity.setAuthority(Enum.valueOf(Authority.class, rs.getString(10)));
          userAuthEntity.getAuthorities().add(authorityEntity);
        }
        return isUserProcessed ? Optional.of(userAuthEntity) : Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findUserInUserdataById(UUID id) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement userSelectPs = conn.prepareStatement("""
          SELECT id,
                 username,
                 currency,
                 firstname,
                 surname,
                 photo
          FROM "user"
          WHERE  id = ?
          """);
      ) {
        userSelectPs.setObject(1, id);
        ResultSet rs = userSelectPs.executeQuery();
        UserEntity userEntity = new UserEntity();
        if (rs.next()) {
          userEntity.setId(rs.getObject(1, UUID.class));
          userEntity.setUsername(rs.getString(2));
          userEntity.setCurrency(Enum.valueOf(CurrencyValues.class, rs.getString(3)));
          userEntity.setFirstname(rs.getString(4));
          userEntity.setSurname(rs.getString(5));
          userEntity.setPhoto(rs.getBytes(6));
          return Optional.of(userEntity);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateUserInAuth(UserAuthEntity userAuthEntity) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          """
                UPDATE "user"
                SET username = ?,
                    password = ?,
                    enabled = ?,
                    account_non_expired = ?,
                    account_non_locked = ?,
                    credentials_non_expired = ?
              WHERE id = ?
              """);
          PreparedStatement authorityDeletePsByAuthority = conn.prepareStatement(
              """
                  DELETE FROM "authority" WHERE  user_id = ? AND authority = ?
                  """);
          PreparedStatement authorityInsertPs = conn.prepareStatement(
              """
                  INSERT INTO "authority"(user_id, authority) VALUES (?, ?)
                   """);
      ) {

        userPs.setString(1, userAuthEntity.getUsername());
        userPs.setString(2, pe.encode(userAuthEntity.getPassword()));
        userPs.setBoolean(3, userAuthEntity.getEnabled());
        userPs.setBoolean(4, userAuthEntity.getAccountNonExpired());
        userPs.setBoolean(5, userAuthEntity.getAccountNonLocked());
        userPs.setBoolean(6, userAuthEntity.getCredentialsNonExpired());
        userPs.setObject(7, userAuthEntity.getId());

        userPs.executeUpdate();

        List<AuthorityEntity> authorityEntities = findAuthoritiesByUserId(userAuthEntity.getId());
        Set<Authority> authoritiesFromDb = mapAuthorityEntitiesToAuthority(authorityEntities);
        Set<Authority> authoritiesCandidate = mapAuthorityEntitiesToAuthority(
            userAuthEntity.getAuthorities());
        Set<Authority> tempSet = new HashSet<>(authoritiesFromDb);
        tempSet.removeAll(authoritiesCandidate);
        if (!tempSet.isEmpty()) {
          for (Authority authority : tempSet) {
            authorityDeletePsByAuthority.setObject(1, userAuthEntity.getId());
            authorityDeletePsByAuthority.setString(2, authority.name());
            authorityDeletePsByAuthority.addBatch();
            authorityDeletePsByAuthority.clearParameters();
          }
          authorityDeletePsByAuthority.executeBatch();
        }
        tempSet = new HashSet<>(authoritiesCandidate);
        tempSet.removeAll(authoritiesFromDb);
        if (!tempSet.isEmpty()) {
          for (Authority authority : tempSet) {
            authorityInsertPs.setObject(1, userAuthEntity.getId());
            authorityInsertPs.setString(2, authority.name());
            authorityInsertPs.addBatch();
            authorityInsertPs.clearParameters();
          }
          authorityInsertPs.executeBatch();
        }
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateUserInUserdata(UserEntity userEntity) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement userUpdatePs = conn.prepareStatement("""
          UPDATE "user"
          SET username = ?,
              currency = ?,
              firstname = ?,
              surname = ?,
              photo = ?
          WHERE id = ?
           """);
      ) {
        userUpdatePs.setString(1, userEntity.getUsername());
        userUpdatePs.setString(2, userEntity.getCurrency().name());
        userUpdatePs.setString(3, userEntity.getFirstname());
        userUpdatePs.setString(4, userEntity.getSurname());
        userUpdatePs.setBytes(5, userEntity.getPhoto());
        userUpdatePs.setObject(6, userEntity.getId());
        userUpdatePs.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
    try (Connection conn = authDs.getConnection()) {
      try (PreparedStatement authoritySelectPs = conn.prepareStatement("""
          SELECT id,
                 user_id,
                 authority
          FROM "authority"
          WHERE  user_id = ?
          """);
      ) {
        authoritySelectPs.setObject(1, userId);
        ResultSet rs = authoritySelectPs.executeQuery();
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          authorityEntities.add(new AuthorityEntity(
              rs.getObject(1, UUID.class),
              rs.getObject(2, UUID.class),
              Enum.valueOf(Authority.class, rs.getString(3))
          ));
        }
        return authorityEntities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Set<Authority> mapAuthorityEntitiesToAuthority(List<AuthorityEntity> authorityEntities) {
    return authorityEntities
        .stream()
        .map(authority -> authority.getAuthority())
        .collect(Collectors.toSet());
  }

}

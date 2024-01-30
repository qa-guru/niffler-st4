package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

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
                            "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
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

                for (Authority authority : Authority.values()) {
                    authorityPs.setObject(1, authUserId);
                    authorityPs.setString(2, authority.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();
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
    public UserAuthEntity readInAuth(String username) {

        try (Connection conn = authDs.getConnection()) {

            try (PreparedStatement userPs = conn.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username=?");
                 PreparedStatement authorityPs = conn.prepareStatement(
                         "SELECT * FROM \"authority\" WHERE user_id = CAST(? AS UUID)")
            ) {

                userPs.setString(1, username);
                userPs.executeQuery();

                UserAuthEntity userAuth = new UserAuthEntity();
                try (ResultSet rs = userPs.getResultSet()) {
                    while (rs.next()) {
                        userAuth.setId(UUID.fromString(rs.getString(1)));
                        userAuth.setUsername(rs.getString(2));
                        userAuth.setPassword(rs.getString(3));
                        userAuth.setEnabled(rs.getBoolean(4));
                        userAuth.setAccountNonExpired(rs.getBoolean(5));
                        userAuth.setAccountNonLocked(rs.getBoolean(6));
                        userAuth.setCredentialsNonExpired(rs.getBoolean(7));
                    }
                }

                authorityPs.setString(1, userAuth.getId().toString());
                authorityPs.executeQuery();

                ArrayList<AuthorityEntity> authorities = new ArrayList<>();
                try (ResultSet rs = authorityPs.getResultSet()) {
                    while (rs.next()) {
                        AuthorityEntity authority = new AuthorityEntity();
                        authority.setId(UUID.fromString(rs.getString("id")));
                        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                        authorities.add(authority);
                    }
                }

                userAuth.setAuthorities(authorities);

                return userAuth;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity readInUserdata(String username) {
        try (Connection conn = udDs.getConnection()) {

            try (PreparedStatement userPs = conn.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username=?")
            ) {

                userPs.setString(1, username);
                userPs.executeQuery();

                UserEntity user = new UserEntity();
                try (ResultSet rs = userPs.getResultSet()) {
                    while (rs.next()) {
                        user.setId(UUID.fromString(rs.getString(1)));
                        user.setUsername(rs.getString(2));
                        user.setCurrency(CurrencyValues.valueOf(rs.getString(3)));
                        user.setFirstname(rs.getString(4));
                        user.setSurname(rs.getString(5));
                        user.setPhoto(rs.getBytes(6));
                    }
                }

                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public UserAuthEntity updateInAuth(UserAuthEntity user) {
        try (Connection conn = authDs.getConnection()) {

            try (PreparedStatement userPs = conn.prepareStatement(
                    "UPDATE \"user\" " +
                            "SET username=?, password=?, enabled=?, account_non_expired=?, account_non_locked=?, credentials_non_expired=? " +
                            "WHERE id=CAST(? AS UUID)")
            ) {

                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.setString(7, user.getId().toString());

                userPs.executeUpdate();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public UserEntity updateInUserdata(UserEntity user) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE \"user\" " +
                            "SET username=?, currency=? " +
                            "WHERE id=?::uuid")) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.setString(3, user.getId().toString());
                ps.executeUpdate();
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

            try (PreparedStatement userPs = conn.prepareStatement(
                    "DELETE FROM \"user\" " +
                            "WHERE id = CAST(? AS UUID)");
                 PreparedStatement authorityPs = conn.prepareStatement(
                         "DELETE FROM \"authority\" " +
                                 "WHERE user_id = CAST(? AS UUID)")
            ) {

                authorityPs.setString(1, id.toString());
                authorityPs.executeUpdate();

                userPs.setString(1, id.toString());
                userPs.executeUpdate();



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
    public void deleteInUserdataById(UUID id) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM \"user\" " +
                            "WHERE id=?::uuid")) {

                ps.setString(1, id.toString());
                ps.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

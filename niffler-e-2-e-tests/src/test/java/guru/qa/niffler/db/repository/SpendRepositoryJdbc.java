package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.SpendEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);
    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection conn = spendDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement spendDs = conn.prepareStatement(
                    "INSERT INTO \"spend\" " +
                            "(username, currency, amount, description, category_id, spend_date) " +
                            "VALUES (?, ?, ?, ?, ?, CURRENT_DATE)", PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement categoryDs = conn.prepareStatement(
                         "INSERT INTO \"category\" " +
                                 "(category, username) " +
                                 "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
            ) {

                categoryDs.setString(1, spend.getCategory().getCategory());
                categoryDs.setString(2, spend.getUsername());
                categoryDs.execute();

                UUID categoryId;
                try (ResultSet keys = categoryDs.getGeneratedKeys()) {
                    if (keys.next()) {
                        categoryId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }

                spendDs.setString(1, spend.getUsername());
                spendDs.setString(2, spend.getCurrency().name());
                spendDs.setDouble(3, spend.getAmount());
                spendDs.setString(4, spend.getDescription());
                spendDs.setObject(5, categoryId);
                spendDs.executeUpdate();

                UUID spendId;
                try (ResultSet keys = spendDs.getGeneratedKeys()) {
                    if (keys.next()) {
                        spendId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }

                conn.commit();
                spend.setId(spendId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spend;
    }
}

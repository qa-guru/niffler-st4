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

public class SpendingRepositoryJdbc implements SpendingRepository {

    private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);

    @Override
    public SpendEntity createSpending(SpendEntity spend) {

        try (Connection conn = spendDs.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO spend " +
                            "(username, spend_date, currency, amount, description, category_id) " +
                            "VALUES(?, ?, ?, ?, ?, ?::uuid);"
                    , PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, spend.getUsername());
                ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
                ps.setString(3, spend.getCurrency().toString());
                ps.setDouble(4, spend.getAmount());
                ps.setString(5, spend.getDescription());
                ps.setString(6, spend.getCategory().getId().toString());

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        spend.setId(UUID.fromString(keys.getString("id")));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return spend;
    }
}

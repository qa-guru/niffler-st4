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

            try (PreparedStatement psSpend = conn.prepareStatement(
                     "INSERT INTO spend " +
                             "(username, spend_date, currency, amount, description, category_id) " +
                             "VALUES(?, ?, ?, ?, ?, ?::uuid);"
                     , PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                spend.getCategory().setId(UUID.fromString(getCategoryById(spend)));

                psSpend.setString(1, spend.getUsername());
                psSpend.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
                psSpend.setString(3, spend.getCurrency().toString());
                psSpend.setDouble(4, spend.getAmount());
                psSpend.setString(5, spend.getDescription());
                psSpend.setString(6, spend.getCategory().getId().toString());

                psSpend.executeUpdate();

                try (ResultSet keys = psSpend.getGeneratedKeys()) {
                    if (keys.next()) {
                        spend.setId(UUID.fromString(keys.getString("id")));
                    } else {
                        throw new IllegalStateException("Can`t find spend id");
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

    private String getCategoryById(SpendEntity spend){
        String result;

        try (Connection conn = spendDs.getConnection()) {

            try (PreparedStatement psCategory = conn.prepareStatement(
                    "SELECT id FROM category " +
                            "WHERE category=? and username=?;"
                    , PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                psCategory.setString(1, spend.getCategory().getCategory());
                psCategory.setString(2, spend.getUsername());

                try (ResultSet keys = psCategory.executeQuery()) {
                    if (keys.next()) {
                        result = keys.getString(1);
                    } else {
                        throw new IllegalStateException("Can`t find category id");
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}

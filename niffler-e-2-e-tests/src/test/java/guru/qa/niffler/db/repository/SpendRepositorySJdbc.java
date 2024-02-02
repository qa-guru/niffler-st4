package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.SpendEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendRepositorySJdbc implements SpendRepository {

    private final TransactionTemplate spendTxt;
    private final JdbcTemplate spendTemplate;


    public SpendRepositorySJdbc() {
        JdbcTransactionManager spendTm = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.dataSource(Database.SPEND)
        );
        this.spendTxt = new TransactionTemplate(spendTm);
        this.spendTemplate = new JdbcTemplate(spendTm.getDataSource());
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        KeyHolder khCategory = new GeneratedKeyHolder();
        KeyHolder khSpend = new GeneratedKeyHolder();
        return spendTxt.execute(status -> {
            spendTemplate.update(con -> {
                PreparedStatement categoryPs = con.prepareStatement(
                        "INSERT INTO \"category\" " +
                                "(category, username) " +
                                "VALUES (?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                );
                categoryPs.setString(1, spend.getCategory().getCategory());
                categoryPs.setString(2, spend.getUsername());
                return categoryPs;
            }, khCategory);
            spendTemplate.update(con -> {
                PreparedStatement spendPs = con.prepareStatement(
                        "INSERT INTO \"spend\" " +
                                "(username, currency, amount, description, category_id, spend_date) " +
                                "VALUES (?, ?, ?, ?, ?, CURRENT_DATE)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                );
                spendPs.setString(1, spend.getUsername());
                spendPs.setString(2, spend.getCurrency().name());
                spendPs.setDouble(3, spend.getAmount());
                spendPs.setString(4, spend.getDescription());
                spendPs.setObject(5, (UUID) khCategory.getKeys().get("id"));
                return spendPs;
            }, khSpend);

            spend.setId((UUID) khSpend.getKeys().get("id"));

           return spend;
        });
    }
}

package de.hybris.bootstrap.ddl.tools;

import java.util.Objects;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class SqlStatementsExecutor
{
    private static final Logger LOG = Logger.getLogger(SqlStatementsExecutor.class);
    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;


    public SqlStatementsExecutor(DataSource dataSource)
    {
        Objects.requireNonNull(dataSource, "dataSource can't be null");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        this.transactionTemplate = new TransactionTemplate((PlatformTransactionManager)txManager);
    }


    public void execute(Iterable<SqlStatement> statements)
    {
        Objects.requireNonNull(statements, "statements can't be null");
        this.transactionTemplate.execute((TransactionCallback)new Object(this, statements));
    }
}

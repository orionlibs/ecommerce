package de.hybris.platform.util.localization.jdbc.executor;

import com.google.common.base.Preconditions;
import de.hybris.platform.util.localization.jdbc.StatementWithParams;
import de.hybris.platform.util.localization.jdbc.StatementsExecutor;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class BatchedStatementsExecutor implements StatementsExecutor
{
    private static final Logger LOG = Logger.getLogger(BatchedStatementsExecutor.class);
    private final ExecutorService executorService;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final Queue<Future<Long>> pendingConfirmations = new LinkedBlockingQueue<>();


    public BatchedStatementsExecutor(ExecutorService executorService, DataSource dataSource)
    {
        Preconditions.checkNotNull(executorService, "executorService can't be null");
        Preconditions.checkNotNull(dataSource, "dataSource can't be null");
        this.executorService = executorService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        this.transactionTemplate = new TransactionTemplate((PlatformTransactionManager)txManager);
        this.transactionTemplate.setIsolationLevel(2);
        this.transactionTemplate.setPropagationBehavior(3);
    }


    public void execute(Iterable<StatementWithParams> statements)
    {
        Preconditions.checkNotNull(statements, "statements can't be null");
        ExecuteStatementsTask task = new ExecuteStatementsTask(this.transactionTemplate, this.jdbcTemplate, statements);
        this.pendingConfirmations.add(this.executorService.submit((Callable<Long>)task));
    }


    public void flush()
    {
        LOG.info("Waiting for all statements to be executed");
        long totalStatementsExecuted = 0L;
        Future<Long> confirmation;
        while((confirmation = this.pendingConfirmations.poll()) != null)
        {
            try
            {
                totalStatementsExecuted += ((Long)confirmation.get()).longValue();
            }
            catch(InterruptedException | java.util.concurrent.ExecutionException e)
            {
                LOG.error("Error during waiting for localization.", e);
            }
        }
        LOG.info("Succesfully executed " + totalStatementsExecuted + " statements.");
    }
}

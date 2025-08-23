package de.hybris.platform.util.localization.jdbc.rx.subscriptions;

import com.google.common.base.Preconditions;
import de.hybris.platform.util.localization.jdbc.LocalizableRow;
import de.hybris.platform.util.localization.jdbc.LocalizableRowsQuery;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class LocalizableRowsSubscription<T extends LocalizableRow> implements Observable.OnSubscribe<T>
{
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final ExecutorService executorService;
    private final LocalizableRowsQuery<T> localizableRowsQuery;


    public LocalizableRowsSubscription(DataSource dataSource, ExecutorService executorService, LocalizableRowsQuery<T> queryWithMapper)
    {
        Preconditions.checkNotNull(dataSource, "dataSource can't be null");
        Preconditions.checkNotNull(executorService, "executorService can't be null");
        Preconditions.checkNotNull(queryWithMapper, "queryWithMapper can't be null");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.executorService = executorService;
        this.localizableRowsQuery = queryWithMapper;
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        this.transactionTemplate = new TransactionTemplate((PlatformTransactionManager)txManager);
        this.transactionTemplate.setIsolationLevel(2);
        this.transactionTemplate.setPropagationBehavior(3);
        this.transactionTemplate.setReadOnly(true);
    }


    public void call(Subscriber<? super T> subscriber)
    {
        this.executorService.execute((Runnable)new Object(this, subscriber));
    }


    private void callInternal(Subscriber<? super T> subscriber)
    {
        try
        {
            this.transactionTemplate.execute((TransactionCallback)new Object(this, subscriber));
        }
        catch(Throwable throwable)
        {
            Exceptions.throwIfFatal(throwable);
            subscriber.onError(throwable);
            return;
        }
        subscriber.onCompleted();
    }
}

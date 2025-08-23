package de.hybris.platform.jdbcwrapper;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.awaitility.Awaitility;

public class DBOutageSimulator
{
    private final ExecutorService executor;
    private final JUnitJDBCConnectionPool connectionPool;
    AtomicInteger outageCounter = new AtomicInteger(0);


    public DBOutageSimulator(JUnitJDBCConnectionPool connectionPool)
    {
        this(Executors.newCachedThreadPool(), connectionPool);
    }


    public DBOutageSimulator(ExecutorService executor, JUnitJDBCConnectionPool connectionPool)
    {
        this.executor = executor;
        this.connectionPool = connectionPool;
    }


    private void enableOutage(JUnitJDBCConnectionPool connectionPool)
    {
        if(this.outageCounter.incrementAndGet() > 0)
        {
            connectionPool.setAllConnectionsFail(true);
        }
    }


    private void disableOutage(JUnitJDBCConnectionPool connectionPool)
    {
        if(this.outageCounter.decrementAndGet() == 0)
        {
            connectionPool.setAllConnectionsFail(false);
        }
    }


    public DBOutage simulateDBOutage(Duration outageDuration)
    {
        return new DBOutage(this.executor.submit(() -> {
            try
            {
                enableOutage(this.connectionPool);
                Thread.sleep(outageDuration.toMillis());
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
            finally
            {
                disableOutage(this.connectionPool);
            }
        } null));
    }


    public DBOutage simulateDBOutage(Supplier<Boolean> until, Duration timeout)
    {
        return new DBOutage(this.executor.submit(() -> {
            try
            {
                enableOutage(this.connectionPool);
                Awaitility.await().atMost(timeout).until(());
            }
            finally
            {
                disableOutage(this.connectionPool);
            }
        } null));
    }


    public DBOutage simulateDBOutage(Supplier<Boolean> until)
    {
        Duration defaultAwaitingTimeout = Duration.ofMinutes(5L);
        return simulateDBOutage(until, defaultAwaitingTimeout);
    }
}

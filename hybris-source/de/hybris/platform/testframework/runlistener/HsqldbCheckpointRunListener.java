package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import java.time.Duration;
import java.time.Instant;
import javax.sql.DataSource;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class HsqldbCheckpointRunListener extends RunListener
{
    private static final Logger LOG = LoggerFactory.getLogger(HsqldbCheckpointRunListener.class);


    public void testFinished(Description description) throws Exception
    {
        HybrisDataSource dataSource = Registry.getCurrentTenant().getDataSource();
        if("hsqldb".equals(dataSource.getDatabaseName()))
        {
            Instant start = Instant.now();
            JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource)dataSource);
            jdbcTemplate.execute("CHECKPOINT");
            LOG.info("Calling CHECKPOINT for HSQLDB took {} ms", Long.valueOf(Duration.between(start, Instant.now()).toMillis()));
        }
    }
}

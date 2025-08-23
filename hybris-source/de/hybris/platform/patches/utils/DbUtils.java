package de.hybris.platform.patches.utils;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jdbcwrapper.HybrisJdbcTemplate;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class DbUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(DbUtils.class);


    private DbUtils()
    {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }


    public static void executeUpdate(String query)
    {
        Objects.requireNonNull(query, "query is required");
        JdbcTemplate template = getJdbcTemplate();
        try
        {
            LOG.info("Executing SQL update: {}", query);
            template.execute(query);
        }
        catch(DataAccessException e)
        {
            throw new IllegalArgumentException("Unable to execute SQL update: " + query, e);
        }
    }


    public static JdbcTemplate getJdbcTemplate()
    {
        return (JdbcTemplate)Registry.getApplicationContext().getBean("jdbcTemplate", HybrisJdbcTemplate.class);
    }
}

package de.hybris.platform.jdbcwrapper;

import de.hybris.platform.core.Registry;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class HybrisJdbcTemplate extends JdbcTemplate
{
    public DataSource getDataSource()
    {
        return (DataSource)Registry.getCurrentTenantNoFallback().getDataSource();
    }
}

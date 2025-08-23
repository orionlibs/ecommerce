package de.hybris.platform.licence.internal;

import com.google.common.base.Strings;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import java.util.Date;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class HybrisLicenceDAO
{
    private static final Logger LOG = Logger.getLogger(HybrisLicenceDAO.class);
    private static final String CREATION_TIME_COL = ServiceCol.CREATED_TS.colName();


    public Date getStartingPointDateForPlatformInstance(HybrisDataSource dataSource)
    {
        JdbcTemplate jdbcTemplate = getJdbcTemplate((DataSource)dataSource);
        String query = getFindCheckDateQuery(dataSource.getTablePrefix());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Check query for min starting point date: " + query);
        }
        Date result = (Date)jdbcTemplate.queryForObject(query, Date.class);
        return result;
    }


    private String getFindCheckDateQuery(String tenantPrefix)
    {
        return FluentSqlBuilder.genericBuilder()
                        .select(new String[] {getMinFunctionForCreatedTs()}).from(getUsersTableName(tenantPrefix)).toSql();
    }


    private String getMinFunctionForCreatedTs()
    {
        return "min(" + CREATION_TIME_COL + ")";
    }


    private String getUsersTableName(String tenantPrefix)
    {
        return Strings.nullToEmpty(tenantPrefix) + "users";
    }


    private JdbcTemplate getJdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }
}

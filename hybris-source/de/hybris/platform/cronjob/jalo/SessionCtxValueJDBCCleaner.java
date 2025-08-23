package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.collections.fast.YLongList;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;

final class SessionCtxValueJDBCCleaner
{
    private static final String ATTR_SESSIONCONTEXTVALUES = "sessioncontextvalues";
    private static final Logger LOG = Logger.getLogger(SessionCtxValueJDBCCleaner.class);
    private static final String JDBC_CLEAN_SESSION_CTX_KEY = "jdbc.clean.sessionContextValues";
    final PreparedStatementCreatorFactory findPsCreatorFactory = new PreparedStatementCreatorFactory(getFindCronJobsQuery());
    final PreparedStatementCreatorFactory deletePsCreatorFactory = new PreparedStatementCreatorFactory(getDeletePropsQuery());
    final FixCronJobRowFinder resultFixHandler = new FixCronJobRowFinder(this);


    public SessionCtxValueJDBCCleaner()
    {
        this.findPsCreatorFactory.addParameter(new SqlParameter(12));
        this.deletePsCreatorFactory.addParameter(new SqlParameter(12));
        this.deletePsCreatorFactory.addParameter(new SqlParameter(-5));
    }


    private String getTenantAwareTableName(String tableName)
    {
        return Config.getString("db.tableprefix", "") + Config.getString("db.tableprefix", "");
    }


    private final String getDeletePropsQuery()
    {
        return "DELETE FROM  " + getTenantAwareTableName("props") + "  WHERE NAME =? AND ITEMPK =?  ";
    }


    private String getFindCronJobsQuery()
    {
        return "SELECT  cj.PK , p.VALUE1 FROM " + getTenantAwareTableName("cronjobs") + " cj JOIN " +
                        getTenantAwareTableName("props") + " p ON cj.PK = p.ITEMPK AND p.NAME=? AND p.VALUESTRING1 is not null AND p.VALUE1 is not null ORDER BY cj.PK";
    }


    public void cleanAttribute()
    {
        if(Config.getParameter("jdbc.clean.sessionContextValues") != null)
        {
            LOG.info("########################################################################################");
            LOG.info("Performing SessionCtxValueJDBCCleaner.\nIf it is not desired, switch the jdbc.clean.sessionContextValues flag to false (or remove it completely)");
            LOG.info("########################################################################################");
            try
            {
                JdbcTemplate template = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
                long start = System.currentTimeMillis();
                template.query(this.findPsCreatorFactory.newPreparedStatementCreator(new Object[] {"sessioncontextvalues"}, ), (RowCallbackHandler)this.resultFixHandler);
                YLongList cronJobsToRemove = this.resultFixHandler.getCronJobsToFix();
                if(cronJobsToRemove.size() > 0)
                {
                    for(int i = 0; i < cronJobsToRemove.size(); i++)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Removing ... " + cronJobsToRemove.get(i));
                        }
                        int result = removeNotSerializableSession(template, cronJobsToRemove.get(i));
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Done ... " + cronJobsToRemove.get(i) + " result  - " + result);
                        }
                    }
                    LOG.info("Performed clean up cronJob.sessioncontextvalues values  ( " + cronJobsToRemove.size() + " rows affected ) in  " + (
                                    System.currentTimeMillis() - start) / 1000L + " seconds.");
                }
            }
            catch(DataAccessException dae)
            {
                LOG.warn("Clean up sessioncontextvalues failed.");
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(dae);
                }
            }
        }
    }


    private int removeNotSerializableSession(JdbcTemplate template, long singleCronJobPK)
    {
        return template.update(this.deletePsCreatorFactory.newPreparedStatementCreator(new Object[] {"sessioncontextvalues",
                        Long.valueOf(singleCronJobPK)}));
    }
}

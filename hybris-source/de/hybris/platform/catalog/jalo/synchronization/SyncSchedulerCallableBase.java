package de.hybris.platform.catalog.jalo.synchronization;

import com.google.common.base.Joiner;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SQLSearchResult;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;

abstract class SyncSchedulerCallableBase implements Callable<Integer>
{
    public static final String SYNCHRONZATION_WITHIN_DEPLOYMENT = "synchronization.within.deployment";
    private static final Logger LOG = Logger.getLogger(SyncSchedulerCallableBase.class.getName());
    protected final SessionContext ctx;
    protected final ComposedType type;
    protected final SyncScheduleWriter writer;
    protected final CatalogVersionSyncJob catalogVersionSyncJob;
    protected final boolean synchronizationWithinDeployment;


    private SyncSchedulerCallableBase()
    {
        this(null, null, null, null);
    }


    public SyncSchedulerCallableBase(CatalogVersionSyncJob catalogVersionSyncJob, SessionContext ctx, ComposedType type, SyncScheduleWriter writer)
    {
        this.catalogVersionSyncJob = catalogVersionSyncJob;
        this.ctx = ctx;
        this.type = type;
        this.writer = writer;
        this.synchronizationWithinDeployment = isSynchronizationWithinDeploymentEnabled();
    }


    protected Map<String, Object> getQueryParameters()
    {
        Map<String, Object> params = new HashMap<>(3);
        if(this.catalogVersionSyncJob.isExclusiveModeAsPrimitive())
        {
            params.put("me", this.catalogVersionSyncJob);
        }
        params.put("srcVer", this.catalogVersionSyncJob.getSourceVersion());
        params.put("tgtVer", this.catalogVersionSyncJob.getTargetVersion());
        return params;
    }


    public Integer call() throws Exception
    {
        try
        {
            SessionContext localSession = JaloSession.getCurrentSession().createLocalSessionContext(this.ctx);
            localSession.setAttribute("disableExecution", Boolean.TRUE);
            return callImpl(localSession);
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
    }


    private Integer callImpl(SessionContext localSessionCtx)
    {
        long startTime = System.currentTimeMillis();
        JDBCQuery jdbcQuery = null;
        try
        {
            SQLSearchResult sqlSearchResult = (SQLSearchResult)FlexibleSearch.getInstance().search(localSessionCtx,
                            handleGenerateQuery(),
                            getQueryParameters(),
                            Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class}, ), true, true, 0, -1);
            jdbcQuery = new JDBCQuery(this.catalogVersionSyncJob.getTenant(), sqlSearchResult.getSQLForPreparedStatement(), sqlSearchResult.getValuesForPreparedStatement(), isUseReadOnlyDatasource());
            jdbcQuery.execute();
            int count = 0;
            while(jdbcQuery.hasNext())
            {
                handleWriteSchedule(jdbcQuery);
                count++;
            }
            if(count > 0)
            {
                handleLogCompletion(count, System.currentTimeMillis() - startTime);
            }
            return Integer.valueOf(count);
        }
        catch(JaloSystemException jse)
        {
            handleLogException((Exception)jse);
            throw jse;
        }
        catch(IOException e)
        {
            handleLogException(e);
            throw new JaloSystemException(e);
        }
        finally
        {
            if(jdbcQuery != null)
            {
                jdbcQuery.close();
            }
        }
    }


    protected void handleLogException(Exception exception)
    {
        LOG.error("Exception " + exception
                        .getMessage() + " occurred during executing query \n(" + handleGenerateQuery() + ") with arguments (" +
                        Joiner.on(",").join(getQueryParameters().keySet()) + "),\nvalues(" +
                        Joiner.on(",").join(getQueryParameters().values()) + ")\n", exception);
    }


    private boolean isUseReadOnlyDatasource()
    {
        CronJob cronJob = (CronJob)JaloSession.getCurrentSession().getAttribute("currentCronJob");
        return (cronJob != null && cronJob.isUseReadOnlyDatasourceAsPrimitive());
    }


    private boolean isSynchronizationWithinDeploymentEnabled()
    {
        return Config.getBoolean("synchronization.within.deployment", true);
    }


    protected abstract String handleGenerateQuery();


    protected abstract void handleWriteSchedule(JDBCQuery paramJDBCQuery) throws IOException;


    protected abstract void handleLogCompletion(int paramInt, long paramLong);


    abstract String generateQueryForType(String paramString);
}

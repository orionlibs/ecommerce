package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.hints.impl.FlexibleSearchHints;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadOnlyConditionsHelper
{
    public static final String PARAM_FS_READ_ONLY_DATASOURCE = "flexiblesearch.readOnly.datasource";
    public static final String CTX_ENABLE_FS_ON_READ_REPLICA = "ctx.enable.fs.on.read-replica";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadOnlyConditionsHelper.class);
    private final Map<String, Optional<HybrisDataSource>> readOnlyDataSources = new ConcurrentHashMap<>();


    protected boolean isTxActive()
    {
        return Transaction.current().isRunning();
    }


    protected boolean isMasterDataSourceActive(Tenant tenant)
    {
        return (!tenant.isAlternativeMasterDataSource() && !tenant.isSlaveDataSource());
    }


    protected Optional<Boolean> readOnlyDataSourceEnabledInSessionContext(Tenant tenant)
    {
        if(!JaloSession.hasCurrentSession(tenant))
        {
            return Optional.empty();
        }
        JaloSession currentSession = JaloSession.getCurrentSession(tenant);
        if(currentSession == null)
        {
            return Optional.empty();
        }
        return readOnlyDataSourceEnabledInSessionContext(currentSession.getSessionContext());
    }


    public Optional<Boolean> readOnlyDataSourceEnabledInSessionContext(SessionContext ctx)
    {
        boolean ret;
        Object attribute = ((SessionContext)Objects.<SessionContext>requireNonNull(ctx)).getAttribute("ctx.enable.fs.on.read-replica");
        if(attribute == null)
        {
            return Optional.empty();
        }
        if(attribute instanceof Boolean)
        {
            ret = ((Boolean)attribute).booleanValue();
        }
        else if(attribute instanceof String)
        {
            String s = ((String)attribute).trim();
            if(Boolean.TRUE.toString().equalsIgnoreCase(s))
            {
                ret = true;
            }
            else if(Boolean.FALSE.toString().equalsIgnoreCase(s))
            {
                ret = false;
            }
            else
            {
                return Optional.empty();
            }
        }
        else
        {
            return Optional.empty();
        }
        return Optional.of(Boolean.valueOf(ret));
    }


    public Optional<HybrisDataSource> getReadOnlyDataSource(Tenant tenant)
    {
        String readOnlyDataSourceId = Config.getString("flexiblesearch.readOnly.datasource", "").toLowerCase(Locale.ROOT);
        if(StringUtils.isBlank(readOnlyDataSourceId))
        {
            return Optional.empty();
        }
        Optional<HybrisDataSource> hybrisDataSource = this.readOnlyDataSources.computeIfAbsent(tenant.getTenantID(), tenantId -> selectReadOnlyDataSource(tenant, readOnlyDataSourceId));
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("selected read-only datasource: {}", hybrisDataSource
                            .<String>map(HybrisDataSource::getID).orElse("unavailable"));
        }
        return hybrisDataSource;
    }


    public boolean isDataSourceReadOnlyReplica(HybrisDataSource hybrisDataSource)
    {
        Optional<HybrisDataSource> readOnlyDataSource = this.readOnlyDataSources.get(hybrisDataSource.getTenant().getTenantID());
        if(readOnlyDataSource != null && readOnlyDataSource.isPresent())
        {
            return ((Boolean)readOnlyDataSource.<Boolean>map(dataSource -> Boolean.valueOf(dataSource.equals(hybrisDataSource))).orElse(Boolean.valueOf(false))).booleanValue();
        }
        return false;
    }


    private Optional<HybrisDataSource> selectReadOnlyDataSource(Tenant tenant, String readOnlyDataSourceId)
    {
        Optional<HybrisDataSource> dataSource = tenant.getAllSlaveDataSources().stream().filter(ds -> ds.getID().equals(readOnlyDataSourceId)).findFirst();
        if(dataSource.isEmpty())
        {
            LOGGER.warn("dataSource {} cannot be used as flexible-search read-only dataSource - there is no dataSource configured with this id", readOnlyDataSourceId);
            return dataSource;
        }
        String mainDatabaseName = tenant.getDataSource().getDatabaseName();
        String readOnlyDatabaseName = ((HybrisDataSource)dataSource.get()).getDatabaseName();
        dataSource = dataSource.filter(ds -> Objects.equals(ds.getDatabaseName(), mainDatabaseName));
        if(dataSource.isEmpty())
        {
            LOGGER.warn("dataSource {} cannot be used as flexible-search read-only dataSource - it uses a different DB provider ({}) than main database ({})", new Object[] {readOnlyDataSourceId, readOnlyDatabaseName, mainDatabaseName});
            return dataSource;
        }
        return dataSource;
    }


    public boolean couldUseReadOnlyDataSource(Tenant tenant, List<Hint> hints)
    {
        if(isTxActive())
        {
            LOGGER.debug("transaction is active - can't use read-only datasource");
            return false;
        }
        if(!isMasterDataSourceActive(tenant))
        {
            LOGGER.debug("current datasource ({}) is not master - can't use read-only datasource", tenant
                            .getDataSource().getID());
            return false;
        }
        Optional<Boolean> hintsIndicator = checkHints(tenant, hints);
        if(hintsIndicator.isPresent() && Boolean.FALSE.equals(hintsIndicator.get()))
        {
            return false;
        }
        return ((Boolean)readOnlyDataSourceEnabledInSessionContext(tenant).or(() -> hintsIndicator).orElse(Boolean.valueOf(false))).booleanValue();
    }


    protected Optional<Boolean> checkHints(Tenant tenant, List<Hint> hints)
    {
        Objects.requireNonNull(FlexibleSearchHints.CategorizedQueryHint.class);
        return hints.stream().filter(h -> h instanceof FlexibleSearchHints.CategorizedQueryHint).map(FlexibleSearchHints.CategorizedQueryHint.class::cast)
                        .map(c -> c.isReadOnlyDataSourcePreferred(tenant))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .reduce(Boolean::logicalAnd);
    }
}

package de.hybris.platform.jalo.flexiblesearch.limit;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.FallbackLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.HanaLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.HsqlLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.MySqlLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.OracleLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.PostgreSqlLimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.impl.SqlServerLimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.util.config.ConfigIntf;

public class LimitStatementBuilderFactory
{
    public static final String DISABLE_SPECIFIC_DB_LIMIT_SUPPORT = "flexible.search.disable.specific.db.limit.support";
    public static final String ENABLE_LIMIT_SUPPORT_ON_SQL_SERVER_2012 = "sqlserver.enableLimitSupportForSQLServer2012";
    private final Tenant tenant;
    private final boolean hanaUsingLiteralLimitParameters;


    public LimitStatementBuilderFactory(Tenant tenant)
    {
        this.tenant = tenant;
        this.hanaUsingLiteralLimitParameters = readHanaLiteralParamFromTenantIfAvailable(tenant);
    }


    protected boolean readHanaLiteralParamFromTenantIfAvailable(Tenant tenant)
    {
        boolean use = true;
        if(tenant != null)
        {
            ConfigIntf cfg = tenant.getConfig();
            if(cfg != null)
            {
                use = cfg.getBoolean("hanadb.hack.limit.parameters.as.literal", true);
            }
        }
        return use;
    }


    public LimitStatementBuilder getLimitStatementBuilder(TranslatedQuery.ExecutableQuery originalQuery, int originalStart, int originalCount)
    {
        boolean paginationNeeded = isPaginationNeeded(originalStart, originalCount);
        boolean specificDbLimitSupportEnabled = isSpecificDbLimitSupportEnabled();
        if(!paginationNeeded || !specificDbLimitSupportEnabled)
        {
            return (LimitStatementBuilder)new FallbackLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        if(isDbUsed("hsqldb"))
        {
            return (LimitStatementBuilder)new HsqlLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        if(isDbUsed("oracle"))
        {
            return (LimitStatementBuilder)new OracleLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        if(isDbUsed("mysql"))
        {
            return (LimitStatementBuilder)new MySqlLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        if(isDbUsed("sap"))
        {
            return (LimitStatementBuilder)new HanaLimitStatementBuilder(originalQuery, originalStart, originalCount, this.hanaUsingLiteralLimitParameters);
        }
        if(isDbUsed("sqlserver") && isLimitSupportEnabledForSqlServer2012())
        {
            return (LimitStatementBuilder)new SqlServerLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        if(isDbUsed("postgresql"))
        {
            return (LimitStatementBuilder)new PostgreSqlLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        return (LimitStatementBuilder)new FallbackLimitStatementBuilder(originalQuery, originalStart, originalCount);
    }


    private boolean isPaginationNeeded(int start, int count)
    {
        return (start > 0 || count > 0);
    }


    protected boolean isDbUsed(String dbName)
    {
        return dbName.equals(this.tenant.getDataSource().getDatabaseName());
    }


    protected boolean isSpecificDbLimitSupportEnabled()
    {
        Boolean disabledLocally = (Boolean)getCurrentSession().getAttribute("flexible.search.disable.specific.db.limit.support");
        return (disabledLocally != null) ? (!disabledLocally.booleanValue()) : (!this.tenant.getConfig().getBoolean("flexible.search.disable.specific.db.limit.support", false));
    }


    protected boolean isLimitSupportEnabledForSqlServer2012()
    {
        return this.tenant.getConfig().getBoolean("sqlserver.enableLimitSupportForSQLServer2012", true);
    }


    private JaloSession getCurrentSession()
    {
        return JaloSession.getCurrentSession(this.tenant);
    }
}

package de.hybris.platform.jdbcwrapper;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Splitter;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.metrics.dropwizard.MetricUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class AzureConnectionFactoryHelper
{
    private static final String AZURE_READ_ONLY_STMT = "SELECT DATABASEPROPERTYEX(DB_NAME(), 'Updateability')";
    public static final String AZURE_READ_ONLY_VALID_VALUE = "READ_ONLY";
    public static final String PROPERTY_DATASOURCES_TO_CHECK_READ_ONLY = "db.azure.checkReadOnlyReplica.datasources";
    private final HybrisDataSource dataSource;
    private final Supplier<Set<String>> datasourcesToCheckReadOnly = (Supplier<Set<String>>)Suppliers.memoize(this::createDataSourcesToCheckIfReadOnly);
    private final Supplier<String> readOnlyCheckMetricNameSupplier = (Supplier<String>)Suppliers.memoize(this::createReadOnlyMetricsName);
    private final MetricRegistry metricRegistry;


    public AzureConnectionFactoryHelper(HybrisDataSource dataSource)
    {
        this(dataSource, null);
    }


    public AzureConnectionFactoryHelper(HybrisDataSource dataSource, MetricRegistry metricRegistry)
    {
        this.dataSource = dataSource;
        this.metricRegistry = metricRegistry;
    }


    private ImmutableSet<String> createDataSourcesToCheckIfReadOnly()
    {
        String config = this.dataSource.getTenant().getConfig().getString("db.azure.checkReadOnlyReplica.datasources", "");
        List<String> dataSources = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(config);
        return ImmutableSet.copyOf(dataSources);
    }


    private String createReadOnlyMetricsName()
    {
        return MetricUtils.metricName("readOnlyCheck", new String[] {"time"}).module("datasource")
                        .extension("core")
                        .tenant(this.dataSource.getTenant().getTenantID())
                        .tag("datasource", this.dataSource.getID())
                        .build();
    }


    public void applyAzureReadOnlySettings(Connection sqlConnection) throws SQLException
    {
        if(!shouldCheckAzureReadOnly())
        {
            return;
        }
        Optional<Timer.Context> timer = getMetricRegistry().map(mr -> mr.timer(this.readOnlyCheckMetricNameSupplier.get()).time());
        try
        {
            PreparedStatement stmt = createReadOnlyCheckStatement(sqlConnection);
            try
            {
                ResultSet rs = stmt.executeQuery();
                try
                {
                    if(rs.next())
                    {
                        String result = rs.getString(1);
                        if("READ_ONLY".equals(result))
                        {
                            sqlConnection.setReadOnly(true);
                        }
                    }
                    if(rs != null)
                    {
                        rs.close();
                    }
                }
                catch(Throwable throwable)
                {
                    if(rs != null)
                    {
                        try
                        {
                            rs.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
                if(stmt != null)
                {
                    stmt.close();
                }
            }
            catch(Throwable throwable)
            {
                if(stmt != null)
                {
                    try
                    {
                        stmt.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        finally
        {
            timer.ifPresent(Timer.Context::stop);
        }
    }


    protected Optional<MetricRegistry> getMetricRegistry()
    {
        return Optional.<MetricRegistry>ofNullable(this.metricRegistry).or(MetricUtils::getMetricRegistry);
    }


    protected PreparedStatement createReadOnlyCheckStatement(Connection sqlConnection) throws SQLException
    {
        return sqlConnection.prepareStatement("SELECT DATABASEPROPERTYEX(DB_NAME(), 'Updateability')");
    }


    protected boolean shouldCheckAzureReadOnly()
    {
        if(!"sqlserver".equalsIgnoreCase(this.dataSource.getDatabaseName()))
        {
            return false;
        }
        return ((Set)this.datasourcesToCheckReadOnly.get()).contains(this.dataSource.getID());
    }
}

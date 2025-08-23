package de.hybris.platform.catalog.jalo.synchronization;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.flexiblesearch.internal.ReadOnlyConditionsHelper;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JDBCQuery
{
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet resultSet;
    private final boolean useReadOnlyDatasource;
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper;
    private final Tenant tenant;
    private final String query;
    private final List<Object> values;
    private final JDBCValueMappings.ValueReader<PK, ?> pkReader;
    private final JDBCValueMappings.ValueReader<Date, ?> dateReader;


    public JDBCQuery(Tenant tenant, String query, List values)
    {
        this(tenant, query, values, false, new ReadOnlyConditionsHelper());
    }


    public JDBCQuery(Tenant tenant, String query, List values, boolean useReadOnlyDatasource)
    {
        this(tenant, query, values, useReadOnlyDatasource, new ReadOnlyConditionsHelper());
    }


    public JDBCQuery(Tenant tenant, String query, List<Object> values, boolean useReadOnlyDatasource, ReadOnlyConditionsHelper readOnlyConditionsHelper)
    {
        this.tenant = tenant;
        this.query = query;
        this.values = values;
        JDBCValueMappings valueMappings = JDBCValueMappings.getInstance();
        this.pkReader = valueMappings.PK_READER;
        this.dateReader = valueMappings.getValueReader(Date.class);
        this.useReadOnlyDatasource = useReadOnlyDatasource;
        this.readOnlyConditionsHelper = readOnlyConditionsHelper;
    }


    public void execute()
    {
        Preconditions.checkArgument((this.resultSet == null));
        try
        {
            this.con = getActiveOrReadOnlyDataSource().getConnection();
            this.stmt = this.con.prepareStatement(this.query);
            if(this.values != null)
            {
                JDBCValueMappings.getInstance().fillStatement(this.stmt, this.values);
            }
            this.resultSet = this.stmt.executeQuery();
        }
        catch(SQLException e)
        {
            throw handleError(e);
        }
    }


    protected HybrisDataSource getActiveOrReadOnlyDataSource()
    {
        if(this.useReadOnlyDatasource)
        {
            Objects.requireNonNull(this.tenant);
            return this.readOnlyConditionsHelper.getReadOnlyDataSource(this.tenant).orElseGet(this.tenant::getDataSource);
        }
        return this.tenant.getDataSource();
    }


    public PK readPK(int pos)
    {
        Preconditions.checkArgument((this.resultSet != null));
        try
        {
            return (PK)this.pkReader.getValue(this.resultSet, pos);
        }
        catch(SQLException e)
        {
            throw handleError(e);
        }
    }


    public Date readDate(int pos)
    {
        Preconditions.checkArgument((this.resultSet != null));
        try
        {
            return (Date)this.dateReader.getValue(this.resultSet, pos);
        }
        catch(SQLException e)
        {
            throw handleError(e);
        }
    }


    public boolean hasNext()
    {
        Preconditions.checkArgument((this.resultSet != null));
        try
        {
            return this.resultSet.next();
        }
        catch(SQLException e)
        {
            throw handleError(e);
        }
    }


    public ResultSet getResultSet()
    {
        Preconditions.checkArgument((this.resultSet != null));
        return this.resultSet;
    }


    protected RuntimeException handleError(SQLException exception)
    {
        close();
        throw new JaloSystemException(exception);
    }


    public void close()
    {
        if(this.resultSet != null)
        {
            try
            {
                this.resultSet.close();
            }
            catch(SQLException sQLException)
            {
            }
            this.resultSet = null;
        }
        if(this.stmt != null)
        {
            try
            {
                this.stmt.close();
            }
            catch(SQLException sQLException)
            {
            }
            this.stmt = null;
        }
        if(this.con != null)
        {
            try
            {
                this.con.close();
            }
            catch(SQLException sQLException)
            {
            }
            this.con = null;
        }
    }
}

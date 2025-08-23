package de.hybris.platform.util.localization.jdbc;

import com.google.common.base.Preconditions;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LocalizableRowsQuery<T extends LocalizableRow>
{
    private final StatementWithParams query;
    protected final String lpTableName;


    public LocalizableRowsQuery(DbInfo dbInfo)
    {
        Preconditions.checkNotNull(dbInfo, "dbInfo can't be null");
        this.query = createQuery(dbInfo);
        this.lpTableName = getLpTableName(dbInfo);
    }


    public StatementWithParams getQuery()
    {
        return this.query;
    }


    public abstract T mapResultSetToRow(ResultSet paramResultSet) throws SQLException;


    protected abstract StatementWithParams createQuery(DbInfo paramDbInfo);


    protected abstract String getLpTableName(DbInfo paramDbInfo);
}

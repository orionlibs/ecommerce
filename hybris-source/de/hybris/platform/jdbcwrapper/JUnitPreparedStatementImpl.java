package de.hybris.platform.jdbcwrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JUnitPreparedStatementImpl extends PreparedStatementImpl
{
    private boolean forceError = false;


    public JUnitPreparedStatementImpl(ConnectionImpl conn, PreparedStatement statement, String query)
    {
        super(conn, statement, query);
    }


    public void setError(boolean hasError)
    {
        this.forceError = hasError;
    }


    protected ResultSet wrapResultSet(ResultSet real) throws SQLException
    {
        if(this.forceError)
        {
            throw new SQLException("test SQL error");
        }
        return super.wrapResultSet(real);
    }
}

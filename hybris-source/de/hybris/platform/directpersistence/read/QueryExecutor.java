package de.hybris.platform.directpersistence.read;

import de.hybris.platform.core.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

public class QueryExecutor implements AutoCloseable
{
    private final Connection conn;


    private QueryExecutor(Connection conn)
    {
        this.conn = conn;
    }


    public static QueryExecutor openConnection()
    {
        try
        {
            return new QueryExecutor(Registry.getCurrentTenant().getMasterDataSource().getConnection());
        }
        catch(SQLException e)
        {
            throw new IllegalStateException("Error during open connection", e);
        }
    }


    <T> T query(String sql, ResultSetExtractor<T> rse, Object... args)
    {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try
        {
            ps = this.conn.prepareStatement(sql);
            ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(args);
            argumentPreparedStatementSetter.setValues(ps);
            rs = ps.executeQuery();
            return (T)rse.extractData(rs);
        }
        catch(SQLException e)
        {
            throw new IllegalStateException("Error during execute query", e);
        }
        finally
        {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
    }


    public void close()
    {
        JdbcUtils.closeConnection(this.conn);
    }
}

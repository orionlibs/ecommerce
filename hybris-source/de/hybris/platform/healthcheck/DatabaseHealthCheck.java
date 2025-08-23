package de.hybris.platform.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import de.hybris.platform.core.Registry;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.support.JdbcUtils;

public class DatabaseHealthCheck extends HybrisHealthCheck
{
    private int timeout;


    protected HealthCheck.Result check()
    {
        Connection connection = null;
        try
        {
            connection = Registry.getCurrentTenant().getDataSource().getConnection();
            return connection.isValid(this.timeout) ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy("DataBase health check failed");
        }
        catch(SQLException e)
        {
            return HealthCheck.Result.unhealthy(e);
        }
        finally
        {
            JdbcUtils.closeConnection(connection);
        }
    }


    @Required
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
}

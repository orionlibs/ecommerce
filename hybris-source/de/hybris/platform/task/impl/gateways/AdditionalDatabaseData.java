package de.hybris.platform.task.impl.gateways;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public final class AdditionalDatabaseData
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalDatabaseData.class);
    private final int majorDbVersion;
    private final int minorDbVersion;


    private AdditionalDatabaseData(int majorDbVersion, int minorDbVersion)
    {
        this.majorDbVersion = majorDbVersion;
        this.minorDbVersion = minorDbVersion;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public static Optional<AdditionalDatabaseData> createAdditionalDatabaseData(JdbcTemplate jdbcTemplate)
    {
        return createAdditionalDatabaseData(jdbcTemplate.getDataSource());
    }


    public static Optional<AdditionalDatabaseData> createAdditionalDatabaseData(DataSource dataSource)
    {
        Objects.requireNonNull(dataSource);
        try
        {
            Connection connection = dataSource.getConnection();
            try
            {
                DatabaseMetaData metaData = connection.getMetaData();
                Optional<AdditionalDatabaseData> optional = Optional.of(
                                builder()
                                                .majorDbVersion(metaData.getDatabaseMajorVersion())
                                                .minorDbVersion(metaData.getDatabaseMinorVersion())
                                                .build());
                if(connection != null)
                {
                    connection.close();
                }
                return optional;
            }
            catch(Throwable throwable)
            {
                if(connection != null)
                {
                    try
                    {
                        connection.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IllegalStateException | java.sql.SQLException e)
        {
            LOGGER.error("could not construct the additional database data", e);
            return Optional.empty();
        }
    }


    public int getMajorDbVersion()
    {
        return this.majorDbVersion;
    }


    public int getMinorDbVersion()
    {
        return this.minorDbVersion;
    }
}

package de.hybris.bootstrap.ddl;

import javax.sql.DataSource;

public interface DataSourceCreator
{
    DataSource createDataSource(DatabaseSettings paramDatabaseSettings);
}

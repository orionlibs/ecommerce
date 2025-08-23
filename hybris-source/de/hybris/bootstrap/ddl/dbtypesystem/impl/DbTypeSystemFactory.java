package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbTypeSystemFactory implements DbTypeSystemProvider
{
    public DbTypeSystem createDbTypeSystem(DataSource dataSource, String tablePrefix, String typeSystemName)
    {
        Preconditions.checkNotNull(dataSource);
        Preconditions.checkNotNull(tablePrefix);
        Preconditions.checkNotNull(typeSystemName);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        DbTypeSystemImpl typeSystem = new DbTypeSystemImpl(getRowsProvider(jdbcTemplate, tablePrefix, typeSystemName));
        typeSystem.initialize();
        return (DbTypeSystem)typeSystem;
    }


    private RowsProvider getRowsProvider(JdbcTemplate jdbcTemplate, String tablePrefix, String typeSystemName)
    {
        return (RowsProvider)new Cached((RowsProvider)new DefaultSQLRowsProvider(jdbcTemplate, tablePrefix, typeSystemName));
    }
}

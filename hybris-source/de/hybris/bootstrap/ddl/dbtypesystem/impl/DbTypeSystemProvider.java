package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import javax.sql.DataSource;

public interface DbTypeSystemProvider
{
    DbTypeSystem createDbTypeSystem(DataSource paramDataSource, String paramString1, String paramString2);
}

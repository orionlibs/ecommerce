package de.hybris.bootstrap.ddl;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;

public interface HybrisPlatform extends Platform
{
    String getTableName(Table paramTable);


    String getColumnName(Column paramColumn);
}

package de.hybris.platform.util.localization.jdbc;

public interface DbInfo
{
    String getTableNameFor(String paramString);


    String getColumnNameFor(String paramString1, String paramString2);
}

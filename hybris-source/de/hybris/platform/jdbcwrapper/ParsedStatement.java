package de.hybris.platform.jdbcwrapper;

import java.sql.SQLException;

public interface ParsedStatement
{
    String parseQuery(String paramString) throws SQLException;
}

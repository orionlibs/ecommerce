package de.hybris.platform.directpersistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public interface BatchCollector
{
    void collectQuery(String paramString, Object... paramVarArgs);


    void collectQuery(String paramString, Object[] paramArrayOfObject, ResultCheck paramResultCheck);


    void collectQuery(String paramString, PreparedStatementSetter paramPreparedStatementSetter);


    void collectQuery(String paramString, PreparedStatementSetter paramPreparedStatementSetter, ResultCheck paramResultCheck);


    void batchUpdate(JdbcTemplate paramJdbcTemplate);
}

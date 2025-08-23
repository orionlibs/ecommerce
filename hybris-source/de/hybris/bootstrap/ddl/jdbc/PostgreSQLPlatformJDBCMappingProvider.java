package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public class PostgreSQLPlatformJDBCMappingProvider extends PlatformJDBCMappingProvider
{
    private final Map<String, JdbcType> jdbcMappings = isChar2CharMappingEnabled() ? (Map<String, JdbcType>)ImmutableMap.of(Character.class
                    .getName(), new JdbcType(1, 10), char.class
                    .getName(), new JdbcType(1, 10, 0, "")) :
                    new HashMap<>();


    public JdbcType getMapping(String javaType)
    {
        JdbcType own = this.jdbcMappings.get(javaType);
        if(own == null)
        {
            return super.getMapping(javaType);
        }
        return own;
    }
}

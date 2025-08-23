package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class OraclePlatformJDBCMappingProvider extends PlatformJDBCMappingProvider
{
    private final Map<String, JdbcType> jdbcMappings = (Map<String, JdbcType>)ImmutableMap.of(String.class
                    .getName(), (new JdbcType(12, 255)).withSizeModifier("CHAR"), String.class
                    .getSimpleName(), (new JdbcType(12, 255)).withSizeModifier("CHAR"));


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

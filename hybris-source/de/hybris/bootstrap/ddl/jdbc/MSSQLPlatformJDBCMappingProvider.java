package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class MSSQLPlatformJDBCMappingProvider extends PlatformJDBCMappingProvider
{
    private static final Logger LOG = Logger.getLogger(MSSQLPlatformJDBCMappingProvider.class);
    private final Map<String, JdbcType> jdbcMappings = (Map<String, JdbcType>)ImmutableMap.of(Character.class
                    .getName(), new JdbcType(1, 4), char.class
                    .getName(), new JdbcType(1, 4, 0, "''"));


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

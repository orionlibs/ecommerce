package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class HSSQLPlatformJDBCMappingProvider extends PlatformJDBCMappingProvider
{
    private static final Logger LOG = Logger.getLogger(HSSQLPlatformJDBCMappingProvider.class);
    private final Map<String, JdbcType> jdbcMappings;


    public HSSQLPlatformJDBCMappingProvider()
    {
        ImmutableMap.Builder<String, JdbcType> mappingBuilder = ImmutableMap.builder();
        mappingBuilder.put(Short.class.getName(), new JdbcType(5, 5));
        mappingBuilder.put(short.class.getName(), new JdbcType(5, 5));
        mappingBuilder.put(Long.class.getName(), new JdbcType(-5, 64, 0));
        mappingBuilder.put(long.class.getName(), new JdbcType(-5, 64, 0, "0"));
        mappingBuilder.put(Boolean.class.getName(), new JdbcType(-6, 3, 0));
        mappingBuilder.put(boolean.class.getName(), new JdbcType(-6, 3, 0, "0"));
        this.jdbcMappings = (Map<String, JdbcType>)mappingBuilder.build();
    }


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

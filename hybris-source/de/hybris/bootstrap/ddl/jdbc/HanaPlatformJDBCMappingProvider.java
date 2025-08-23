package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class HanaPlatformJDBCMappingProvider extends PlatformJDBCMappingProvider
{
    private final Map<String, JdbcType> jdbcMappings;


    public HanaPlatformJDBCMappingProvider()
    {
        ImmutableMap.Builder<String, JdbcType> mappingBuilder = ImmutableMap.builder();
        mappingBuilder.put("HYBRIS.LONG_STRING", new JdbcType(12000, 5000));
        mappingBuilder.put("de.hybris.platform.util.ItemPropertyValueCollection", new JdbcType(12000, 5000));
        mappingBuilder.put("HYBRIS.COMMA_SEPARATED_PKS", new JdbcType(12000, 5000));
        mappingBuilder.put(Class.class.getName(), new JdbcType(12000, 5000));
        mappingBuilder.put(Boolean.class.getName(), new JdbcType(3, 1, 0));
        mappingBuilder.put(boolean.class.getName(), new JdbcType(3, 1, 0, "0"));
        mappingBuilder.put(Integer.class.getName(), new JdbcType(-5, 20, 0));
        mappingBuilder.put(int.class.getName(), new JdbcType(-5, 20, 0, "0"));
        mappingBuilder.put(String.class.getName(), new JdbcType(-9, 255));
        mappingBuilder.put(String.class.getSimpleName(), new JdbcType(-9, 255));
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

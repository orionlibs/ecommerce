package de.hybris.bootstrap.ddl.jdbc;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.ddl.tools.MappingProviderSettings;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;

public class PlatformJDBCMappingProvider
{
    private static final Logger LOG = Logger.getLogger(PlatformJDBCMappingProvider.class);
    public static final int HYBRIS_LONG_STRING = 12000;
    public static final int HYBRIS_COMMA_SEPARATED_PKS = 12001;
    public static final int HYBRIS_PK = 12002;
    public static final int HYBRIS_JSON = 12003;
    private static final String HYBRIS_PK_LITERAL = "HYBRIS.PK";
    protected static final String HYBRIS_LONG_STRING_LITERAL = "HYBRIS.LONG_STRING";
    protected static final String HYBRIS_JSON_LITERAL = "HYBRIS.JSON";
    protected static final String HYBRIS_COMMA_SEPARATED_PKS_LITERAL = "HYBRIS.COMMA_SEPARATED_PKS";
    public static final int MAX_COLUMN_NAME_LENGTH = 30;
    private static final String PK_CLASS_NAME = "de.hybris.platform.core.PK";
    private static final String ITEM_PROPERTY_VALUE_CLASS_NAME = "de.hybris.platform.util.ItemPropertyValue";
    protected static final String ITEM_PROPERTY_VALUE_COLLECTION_CLASS_NAME = "de.hybris.platform.util.ItemPropertyValueCollection";
    private final Map<String, JdbcType> jdbcMappings;


    public PlatformJDBCMappingProvider()
    {
        CommonHybrisTypeMap.register();
        ImmutableMap.Builder<String, JdbcType> mappingBuilder = ImmutableMap.builder();
        mappingBuilder.put(BigDecimal.class.getName(), new JdbcType(3, 30, 8));
        mappingBuilder.put(Boolean.class.getName(), new JdbcType(-7, 1, 0));
        mappingBuilder.put(boolean.class.getName(), new JdbcType(-7, 1, 0, "0"));
        mappingBuilder.put(Byte.class.getName(), new JdbcType(5, 10, 0));
        mappingBuilder.put(byte.class.getName(), new JdbcType(5, 10, 0, "0"));
        if(isChar2CharMappingEnabled())
        {
            mappingBuilder.put(Character.class.getName(), new JdbcType(1, 10, 0));
            mappingBuilder.put(char.class.getName(), new JdbcType(1, 10, 0, "''"));
        }
        else
        {
            mappingBuilder.put(Character.class.getName(), new JdbcType(5, 10, 0));
            mappingBuilder.put(char.class.getName(), new JdbcType(5, 10, 0, "''"));
        }
        mappingBuilder.put(Date.class.getName(), new JdbcType(93));
        mappingBuilder.put(Double.class.getName(), new JdbcType(8, 30, 8));
        mappingBuilder.put(double.class.getName(), new JdbcType(8, 30, 8, "0"));
        mappingBuilder.put(Float.class.getName(), new JdbcType(6, 20, 5));
        mappingBuilder.put(float.class.getName(), new JdbcType(6, 20, 5, "0"));
        mappingBuilder.put(Integer.class.getName(), new JdbcType(4, 20, 0));
        mappingBuilder.put(int.class.getName(), new JdbcType(4, 20, 0, "0"));
        mappingBuilder.put(Short.class.getName(), new JdbcType(4, 10, 0));
        mappingBuilder.put(short.class.getName(), new JdbcType(4, 10, 0, "0"));
        mappingBuilder.put(Long.class.getName(), new JdbcType(-5, 20, 0));
        mappingBuilder.put(long.class.getName(), new JdbcType(-5, 20, 0, "0"));
        mappingBuilder.put(Object.class.getName(), new JdbcType(2004));
        mappingBuilder.put(Serializable.class.getName(), new JdbcType(2004));
        mappingBuilder.put(String.class.getName(), new JdbcType(12, 255));
        mappingBuilder.put(String.class.getSimpleName(), new JdbcType(12, 255));
        mappingBuilder.put("de.hybris.platform.core.PK", new JdbcType(12002, 20, 0));
        mappingBuilder.put("HYBRIS.PK", new JdbcType(12002, 20, 0));
        mappingBuilder.put("de.hybris.platform.util.ItemPropertyValue", new JdbcType(12002, 20, 0));
        mappingBuilder.put("HYBRIS.LONG_STRING", new JdbcType(12000, 4000));
        mappingBuilder.put("de.hybris.platform.util.ItemPropertyValueCollection", new JdbcType(12000, 4000));
        mappingBuilder.put("HYBRIS.COMMA_SEPARATED_PKS", new JdbcType(12000, 4000));
        mappingBuilder.put(Class.class.getName(), new JdbcType(12000, 4000));
        mappingBuilder.put("HYBRIS.JSON", new JdbcType(12003));
        this.jdbcMappings = (Map<String, JdbcType>)mappingBuilder.build();
    }


    protected boolean isChar2CharMappingEnabled()
    {
        return MappingProviderSettings.IS_CHAR_2_CHAR_MAPPING_ENABLED;
    }


    public JdbcType getMapping(String javaType)
    {
        return this.jdbcMappings.get(javaType);
    }
}

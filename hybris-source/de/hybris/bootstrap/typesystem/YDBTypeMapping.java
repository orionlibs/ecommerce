package de.hybris.bootstrap.typesystem;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class YDBTypeMapping extends YNameSpaceElement
{
    private final String databaseName;
    private String isNull;
    private String isNotNull;
    private String primaryKey;
    private Map<String, String> mappings;


    public YDBTypeMapping(YNamespace container, String databaseName)
    {
        super(container);
        this.databaseName = databaseName;
    }


    public String getIsNotNull()
    {
        return this.isNotNull;
    }


    public void setIsNotNull(String isNotNull)
    {
        this.isNotNull = isNotNull;
    }


    public String getPrimaryKey()
    {
        return this.primaryKey;
    }


    public void setPrimaryKey(String pkDef)
    {
        this.primaryKey = pkDef;
    }


    public String getIsNull()
    {
        return this.isNull;
    }


    public void setIsNull(String isNull)
    {
        this.isNull = isNull;
    }


    public String getDatabaseName()
    {
        return this.databaseName;
    }


    public Map<String, String> getTypeMappings()
    {
        return (this.mappings != null) ? this.mappings : Collections.EMPTY_MAP;
    }


    private void add(Map<String, String> mappings, String className)
    {
        add(mappings, className, className);
    }


    private void add(Map<String, String> mappings, String className, String typeStr)
    {
        if(mappings != null && mappings.get(typeStr) != null)
        {
            this.mappings.put(className, mappings.get(typeStr));
        }
    }


    private static final String[][] DEFAULT_TYPES = new String[][] {
                    {String.class
                                    .getName()}, {Class.class
                    .getName(), "HYBRIS.LONG_STRING"}, {Class.class
                    .getName(), "HYBRIS.JSON"}, {Integer.class
                    .getName()}, {int.class
                    .getName()}, {Byte.class
                    .getName()}, {byte.class
                    .getName()}, {Short.class
                    .getName()}, {short.class
                    .getName()}, {Character.class
                    .getName()},
                    {char.class
                                    .getName()}, {Double.class
                    .getName()}, {double.class
                    .getName()}, {Boolean.class
                    .getName()}, {boolean.class
                    .getName()}, {Long.class
                    .getName()}, {long.class
                    .getName()}, {Float.class
                    .getName()}, {float.class
                    .getName()}, {Date.class
                    .getName()},
                    {Color.class
                                    .getName(), Integer.class.getName()}, {Serializable.class
                    .getName()}, {Object.class
                    .getName(), Serializable.class.getName()}, {BigDecimal.class
                    .getName()}, {"de.hybris.platform.core.PK", "HYBRIS.PK"}, {"de.hybris.platform.util.ItemPropertyValue", "HYBRIS.PK"}, {"de.hybris.platform.util.ItemPropertyValueCollection", "HYBRIS.COMMA_SEPARATED_PKS"}};


    public void addTypeMappings(Map<String, String> newOnes)
    {
        if(this.mappings == null)
        {
            this.mappings = new HashMap<>();
        }
        for(String[] defaultType : DEFAULT_TYPES)
        {
            if(defaultType.length == 2)
            {
                add(newOnes, defaultType[0], defaultType[1]);
            }
            else
            {
                add(newOnes, defaultType[0]);
            }
        }
        for(Map.Entry<String, String> entry : newOnes.entrySet())
        {
            if(!this.mappings.containsKey(entry.getKey()))
            {
                add(newOnes, entry.getKey());
            }
        }
    }


    public String getTypeMapping(String type)
    {
        String ret = getTypeMappings().get(type);
        if(ret == null)
        {
            if("de.hybris.platform.util.ItemPropertyValue".equalsIgnoreCase(type) || "de.hybris.platform.core.PK"
                            .equalsIgnoreCase(type))
            {
                ret = getTypeMappings().get("HYBRIS.PK");
            }
            else if("de.hybris.platform.util.ItemPropertyValueCollection".equalsIgnoreCase(type))
            {
                ret = getTypeMappings().get("HYBRIS.COMMA_SEPARATED_PKS");
            }
            else
            {
                ret = getTypeMappings().get(Serializable.class.getName());
            }
        }
        return ret;
    }
}

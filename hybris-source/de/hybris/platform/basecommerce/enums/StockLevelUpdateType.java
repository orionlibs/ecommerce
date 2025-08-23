package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StockLevelUpdateType implements HybrisEnumValue
{
    public static final String _TYPECODE = "StockLevelUpdateType";
    public static final String SIMPLE_CLASSNAME = "StockLevelUpdateType";
    private static final ConcurrentMap<String, StockLevelUpdateType> cache = new ConcurrentHashMap<>();
    public static final StockLevelUpdateType WAREHOUSE = valueOf("warehouse");
    public static final StockLevelUpdateType CUSTOMER_RESERVE = valueOf("customer_reserve");
    public static final StockLevelUpdateType CUSTOMER_RELEASE = valueOf("customer_release");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private StockLevelUpdateType(String code)
    {
        this.code = code.intern();
        this.codeLowerCase = this.code.toLowerCase().intern();
    }


    public boolean equals(Object obj)
    {
        try
        {
            HybrisEnumValue enum2 = (HybrisEnumValue)obj;
            return (this == enum2 || (enum2 != null &&
                            !getClass().isEnum() && !enum2.getClass().isEnum() &&
                            getType().equalsIgnoreCase(enum2.getType()) && getCode().equalsIgnoreCase(enum2.getCode())));
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "StockLevelUpdateType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static StockLevelUpdateType valueOf(String code)
    {
        String key = code.toLowerCase();
        StockLevelUpdateType result = cache.get(key);
        if(result == null)
        {
            StockLevelUpdateType newValue = new StockLevelUpdateType(code);
            StockLevelUpdateType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

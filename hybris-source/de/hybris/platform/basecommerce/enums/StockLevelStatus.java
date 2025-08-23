package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StockLevelStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "StockLevelStatus";
    public static final String SIMPLE_CLASSNAME = "StockLevelStatus";
    private static final ConcurrentMap<String, StockLevelStatus> cache = new ConcurrentHashMap<>();
    public static final StockLevelStatus INSTOCK = valueOf("inStock");
    public static final StockLevelStatus LOWSTOCK = valueOf("lowStock");
    public static final StockLevelStatus OUTOFSTOCK = valueOf("outOfStock");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private StockLevelStatus(String code)
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
        return "StockLevelStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static StockLevelStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        StockLevelStatus result = cache.get(key);
        if(result == null)
        {
            StockLevelStatus newValue = new StockLevelStatus(code);
            StockLevelStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

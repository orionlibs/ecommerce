package de.hybris.platform.europe1.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProductPriceGroup implements HybrisEnumValue
{
    public static final String _TYPECODE = "ProductPriceGroup";
    public static final String SIMPLE_CLASSNAME = "ProductPriceGroup";
    private static final ConcurrentMap<String, ProductPriceGroup> cache = new ConcurrentHashMap<>();
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ProductPriceGroup(String code)
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
        return "ProductPriceGroup";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ProductPriceGroup valueOf(String code)
    {
        String key = code.toLowerCase();
        ProductPriceGroup result = cache.get(key);
        if(result == null)
        {
            ProductPriceGroup newValue = new ProductPriceGroup(code);
            ProductPriceGroup previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

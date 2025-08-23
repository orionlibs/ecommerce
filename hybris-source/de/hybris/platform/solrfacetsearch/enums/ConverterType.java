package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConverterType implements HybrisEnumValue
{
    public static final String _TYPECODE = "ConverterType";
    public static final String SIMPLE_CLASSNAME = "ConverterType";
    private static final ConcurrentMap<String, ConverterType> cache = new ConcurrentHashMap<>();
    public static final ConverterType DEFAULT = valueOf("DEFAULT");
    public static final ConverterType STOREFRONT = valueOf("STOREFRONT");
    public static final ConverterType CUSTOMER_SERVICE = valueOf("CUSTOMER_SERVICE");
    public static final ConverterType COCKPIT = valueOf("COCKPIT");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ConverterType(String code)
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
        return "ConverterType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ConverterType valueOf(String code)
    {
        String key = code.toLowerCase();
        ConverterType result = cache.get(key);
        if(result == null)
        {
            ConverterType newValue = new ConverterType(code);
            ConverterType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

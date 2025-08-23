package de.hybris.platform.commerceservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CountryType implements HybrisEnumValue
{
    public static final String _TYPECODE = "CountryType";
    public static final String SIMPLE_CLASSNAME = "CountryType";
    private static final ConcurrentMap<String, CountryType> cache = new ConcurrentHashMap<>();
    public static final CountryType SHIPPING = valueOf("SHIPPING");
    public static final CountryType BILLING = valueOf("BILLING");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CountryType(String code)
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
        return "CountryType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CountryType valueOf(String code)
    {
        String key = code.toLowerCase();
        CountryType result = cache.get(key);
        if(result == null)
        {
            CountryType newValue = new CountryType(code);
            CountryType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

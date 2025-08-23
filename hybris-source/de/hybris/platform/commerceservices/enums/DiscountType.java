package de.hybris.platform.commerceservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DiscountType implements HybrisEnumValue
{
    public static final String _TYPECODE = "DiscountType";
    public static final String SIMPLE_CLASSNAME = "DiscountType";
    private static final ConcurrentMap<String, DiscountType> cache = new ConcurrentHashMap<>();
    public static final DiscountType PERCENT = valueOf("PERCENT");
    public static final DiscountType ABSOLUTE = valueOf("ABSOLUTE");
    public static final DiscountType TARGET = valueOf("TARGET");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private DiscountType(String code)
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
        return "DiscountType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static DiscountType valueOf(String code)
    {
        String key = code.toLowerCase();
        DiscountType result = cache.get(key);
        if(result == null)
        {
            DiscountType newValue = new DiscountType(code);
            DiscountType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

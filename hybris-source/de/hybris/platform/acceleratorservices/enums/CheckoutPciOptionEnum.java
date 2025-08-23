package de.hybris.platform.acceleratorservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CheckoutPciOptionEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "CheckoutPciOptionEnum";
    public static final String SIMPLE_CLASSNAME = "CheckoutPciOptionEnum";
    private static final ConcurrentMap<String, CheckoutPciOptionEnum> cache = new ConcurrentHashMap<>();
    public static final CheckoutPciOptionEnum DEFAULT = valueOf("Default");
    public static final CheckoutPciOptionEnum HOP = valueOf("HOP");
    public static final CheckoutPciOptionEnum SOP = valueOf("SOP");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CheckoutPciOptionEnum(String code)
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
        return "CheckoutPciOptionEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CheckoutPciOptionEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        CheckoutPciOptionEnum result = cache.get(key);
        if(result == null)
        {
            CheckoutPciOptionEnum newValue = new CheckoutPciOptionEnum(code);
            CheckoutPciOptionEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

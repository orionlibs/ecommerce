package de.hybris.platform.commerceservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CustomerType implements HybrisEnumValue
{
    public static final String _TYPECODE = "CustomerType";
    public static final String SIMPLE_CLASSNAME = "CustomerType";
    private static final ConcurrentMap<String, CustomerType> cache = new ConcurrentHashMap<>();
    public static final CustomerType GUEST = valueOf("GUEST");
    public static final CustomerType REGISTERED = valueOf("REGISTERED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CustomerType(String code)
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
        return "CustomerType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CustomerType valueOf(String code)
    {
        String key = code.toLowerCase();
        CustomerType result = cache.get(key);
        if(result == null)
        {
            CustomerType newValue = new CustomerType(code);
            CustomerType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

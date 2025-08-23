package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReturnFulfillmentStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "ReturnFulfillmentStatus";
    public static final String SIMPLE_CLASSNAME = "ReturnFulfillmentStatus";
    private static final ConcurrentMap<String, ReturnFulfillmentStatus> cache = new ConcurrentHashMap<>();
    public static final ReturnFulfillmentStatus INITIAL = valueOf("INITIAL");
    public static final ReturnFulfillmentStatus CLOSED = valueOf("CLOSED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ReturnFulfillmentStatus(String code)
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
        return "ReturnFulfillmentStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ReturnFulfillmentStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        ReturnFulfillmentStatus result = cache.get(key);
        if(result == null)
        {
            ReturnFulfillmentStatus newValue = new ReturnFulfillmentStatus(code);
            ReturnFulfillmentStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

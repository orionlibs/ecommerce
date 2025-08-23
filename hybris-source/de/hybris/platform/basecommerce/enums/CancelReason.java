package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CancelReason implements HybrisEnumValue
{
    public static final String _TYPECODE = "CancelReason";
    public static final String SIMPLE_CLASSNAME = "CancelReason";
    private static final ConcurrentMap<String, CancelReason> cache = new ConcurrentHashMap<>();
    public static final CancelReason OUTOFSTOCK = valueOf("OutOfStock");
    public static final CancelReason LATEDELIVERY = valueOf("LateDelivery");
    public static final CancelReason WAREHOUSE = valueOf("Warehouse");
    public static final CancelReason CUSTOMERREQUEST = valueOf("CustomerRequest");
    public static final CancelReason OTHER = valueOf("Other");
    public static final CancelReason NA = valueOf("NA");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CancelReason(String code)
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
        return "CancelReason";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CancelReason valueOf(String code)
    {
        String key = code.toLowerCase();
        CancelReason result = cache.get(key);
        if(result == null)
        {
            CancelReason newValue = new CancelReason(code);
            CancelReason previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

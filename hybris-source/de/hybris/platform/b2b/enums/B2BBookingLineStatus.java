package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class B2BBookingLineStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "B2BBookingLineStatus";
    public static final String SIMPLE_CLASSNAME = "B2BBookingLineStatus";
    private static final ConcurrentMap<String, B2BBookingLineStatus> cache = new ConcurrentHashMap<>();
    public static final B2BBookingLineStatus OPEN = valueOf("OPEN");
    public static final B2BBookingLineStatus INVOICED = valueOf("INVOICED");
    public static final B2BBookingLineStatus PENDINGINVOICE = valueOf("PENDINGINVOICE");
    public static final B2BBookingLineStatus DISABLED = valueOf("DISABLED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private B2BBookingLineStatus(String code)
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
        return "B2BBookingLineStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static B2BBookingLineStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        B2BBookingLineStatus result = cache.get(key);
        if(result == null)
        {
            B2BBookingLineStatus newValue = new B2BBookingLineStatus(code);
            B2BBookingLineStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

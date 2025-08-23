package de.hybris.platform.ticket.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsEventReason implements HybrisEnumValue
{
    public static final String _TYPECODE = "CsEventReason";
    public static final String SIMPLE_CLASSNAME = "CsEventReason";
    private static final ConcurrentMap<String, CsEventReason> cache = new ConcurrentHashMap<>();
    public static final CsEventReason COMPLAINT = valueOf("Complaint");
    public static final CsEventReason UPDATE = valueOf("Update");
    public static final CsEventReason FIRSTCONTACT = valueOf("FirstContact");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CsEventReason(String code)
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
        return "CsEventReason";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CsEventReason valueOf(String code)
    {
        String key = code.toLowerCase();
        CsEventReason result = cache.get(key);
        if(result == null)
        {
            CsEventReason newValue = new CsEventReason(code);
            CsEventReason previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

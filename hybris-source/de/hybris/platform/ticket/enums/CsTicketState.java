package de.hybris.platform.ticket.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsTicketState implements HybrisEnumValue
{
    public static final String _TYPECODE = "CsTicketState";
    public static final String SIMPLE_CLASSNAME = "CsTicketState";
    private static final ConcurrentMap<String, CsTicketState> cache = new ConcurrentHashMap<>();
    public static final CsTicketState NEW = valueOf("New");
    public static final CsTicketState OPEN = valueOf("Open");
    public static final CsTicketState CLOSED = valueOf("Closed");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CsTicketState(String code)
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
        return "CsTicketState";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CsTicketState valueOf(String code)
    {
        String key = code.toLowerCase();
        CsTicketState result = cache.get(key);
        if(result == null)
        {
            CsTicketState newValue = new CsTicketState(code);
            CsTicketState previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

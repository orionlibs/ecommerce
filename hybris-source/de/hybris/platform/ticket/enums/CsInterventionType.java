package de.hybris.platform.ticket.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsInterventionType implements HybrisEnumValue
{
    public static final String _TYPECODE = "CsInterventionType";
    public static final String SIMPLE_CLASSNAME = "CsInterventionType";
    private static final ConcurrentMap<String, CsInterventionType> cache = new ConcurrentHashMap<>();
    public static final CsInterventionType EMAIL = valueOf("Email");
    public static final CsInterventionType CALL = valueOf("Call");
    public static final CsInterventionType IM = valueOf("IM");
    public static final CsInterventionType TICKETMESSAGE = valueOf("TicketMessage");
    public static final CsInterventionType PRIVATE = valueOf("Private");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CsInterventionType(String code)
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
        return "CsInterventionType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CsInterventionType valueOf(String code)
    {
        String key = code.toLowerCase();
        CsInterventionType result = cache.get(key);
        if(result == null)
        {
            CsInterventionType newValue = new CsInterventionType(code);
            CsInterventionType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.ticket.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsTicketCategory implements HybrisEnumValue
{
    public static final String _TYPECODE = "CsTicketCategory";
    public static final String SIMPLE_CLASSNAME = "CsTicketCategory";
    private static final ConcurrentMap<String, CsTicketCategory> cache = new ConcurrentHashMap<>();
    public static final CsTicketCategory PROBLEM = valueOf("Problem");
    public static final CsTicketCategory INCIDENT = valueOf("Incident");
    public static final CsTicketCategory COMPLAINT = valueOf("Complaint");
    public static final CsTicketCategory FRAUD = valueOf("Fraud");
    public static final CsTicketCategory NOTE = valueOf("Note");
    public static final CsTicketCategory ENQUIRY = valueOf("Enquiry");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CsTicketCategory(String code)
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
        return "CsTicketCategory";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CsTicketCategory valueOf(String code)
    {
        String key = code.toLowerCase();
        CsTicketCategory result = cache.get(key);
        if(result == null)
        {
            CsTicketCategory newValue = new CsTicketCategory(code);
            CsTicketCategory previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

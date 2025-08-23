package de.hybris.platform.ticket.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsResolutionType implements HybrisEnumValue
{
    public static final String _TYPECODE = "CsResolutionType";
    public static final String SIMPLE_CLASSNAME = "CsResolutionType";
    private static final ConcurrentMap<String, CsResolutionType> cache = new ConcurrentHashMap<>();
    public static final CsResolutionType CLOSED = valueOf("Closed");
    public static final CsResolutionType CLOSEDDUPLICATE = valueOf("ClosedDuplicate");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private CsResolutionType(String code)
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
        return "CsResolutionType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static CsResolutionType valueOf(String code)
    {
        String key = code.toLowerCase();
        CsResolutionType result = cache.get(key);
        if(result == null)
        {
            CsResolutionType newValue = new CsResolutionType(code);
            CsResolutionType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

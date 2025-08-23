package de.hybris.platform.outboundservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OutboundSource implements HybrisEnumValue
{
    public static final String _TYPECODE = "OutboundSource";
    public static final String SIMPLE_CLASSNAME = "OutboundSource";
    private static final ConcurrentMap<String, OutboundSource> cache = new ConcurrentHashMap<>();
    public static final OutboundSource UNKNOWN = valueOf("UNKNOWN");
    public static final OutboundSource OUTBOUNDSYNC = valueOf("OUTBOUNDSYNC");
    public static final OutboundSource WEBHOOKSERVICES = valueOf("WEBHOOKSERVICES");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private OutboundSource(String code)
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
        return "OutboundSource";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static OutboundSource valueOf(String code)
    {
        String key = code.toLowerCase();
        OutboundSource result = cache.get(key);
        if(result == null)
        {
            OutboundSource newValue = new OutboundSource(code);
            OutboundSource previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.ticket.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventType implements HybrisEnumValue
{
    public static final String _TYPECODE = "EventType";
    public static final String SIMPLE_CLASSNAME = "EventType";
    private static final ConcurrentMap<String, EventType> cache = new ConcurrentHashMap<>();
    public static final EventType EVENTS = valueOf("EVENTS");
    public static final EventType START_SESSION_EVENT = valueOf("START_SESSION_EVENT");
    public static final EventType END_SESSION_EVENT = valueOf("END_SESSION_EVENT");
    public static final EventType AGENT_LOGIN = valueOf("AGENT_LOGIN");
    public static final EventType AGENT_LOGOUT = valueOf("AGENT_LOGOUT");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private EventType(String code)
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
        return "EventType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static EventType valueOf(String code)
    {
        String key = code.toLowerCase();
        EventType result = cache.get(key);
        if(result == null)
        {
            EventType newValue = new EventType(code);
            EventType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

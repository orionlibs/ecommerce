package de.hybris.platform.commerceservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuoteNotificationType implements HybrisEnumValue
{
    public static final String _TYPECODE = "QuoteNotificationType";
    public static final String SIMPLE_CLASSNAME = "QuoteNotificationType";
    private static final ConcurrentMap<String, QuoteNotificationType> cache = new ConcurrentHashMap<>();
    public static final QuoteNotificationType EXPIRING_SOON = valueOf("EXPIRING_SOON");
    public static final QuoteNotificationType EXPIRED = valueOf("EXPIRED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private QuoteNotificationType(String code)
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
        return "QuoteNotificationType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static QuoteNotificationType valueOf(String code)
    {
        String key = code.toLowerCase();
        QuoteNotificationType result = cache.get(key);
        if(result == null)
        {
            QuoteNotificationType newValue = new QuoteNotificationType(code);
            QuoteNotificationType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

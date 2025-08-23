package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BookingType implements HybrisEnumValue
{
    public static final String _TYPECODE = "BookingType";
    public static final String SIMPLE_CLASSNAME = "BookingType";
    private static final ConcurrentMap<String, BookingType> cache = new ConcurrentHashMap<>();
    public static final BookingType ENTRY = valueOf("ENTRY");
    public static final BookingType SHIPPING = valueOf("SHIPPING");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private BookingType(String code)
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
        return "BookingType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static BookingType valueOf(String code)
    {
        String key = code.toLowerCase();
        BookingType result = cache.get(key);
        if(result == null)
        {
            BookingType newValue = new BookingType(code);
            BookingType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

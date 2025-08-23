package de.hybris.platform.core.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DeliveryStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "DeliveryStatus";
    public static final String SIMPLE_CLASSNAME = "DeliveryStatus";
    private static final ConcurrentMap<String, DeliveryStatus> cache = new ConcurrentHashMap<>();
    public static final DeliveryStatus NOTSHIPPED = valueOf("NOTSHIPPED");
    public static final DeliveryStatus PARTSHIPPED = valueOf("PARTSHIPPED");
    public static final DeliveryStatus SHIPPED = valueOf("SHIPPED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private DeliveryStatus(String code)
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
        return "DeliveryStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static DeliveryStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        DeliveryStatus result = cache.get(key);
        if(result == null)
        {
            DeliveryStatus newValue = new DeliveryStatus(code);
            DeliveryStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReplacementReason implements HybrisEnumValue
{
    public static final String _TYPECODE = "ReplacementReason";
    public static final String SIMPLE_CLASSNAME = "ReplacementReason";
    private static final ConcurrentMap<String, ReplacementReason> cache = new ConcurrentHashMap<>();
    public static final ReplacementReason RETURNINTIME = valueOf("ReturnInTime");
    public static final ReplacementReason DAMAGEDINTRANSIT = valueOf("DamagedInTransit");
    public static final ReplacementReason LATEDELIVERY = valueOf("LateDelivery");
    public static final ReplacementReason MANUFACTURINGFAULT = valueOf("ManufacturingFault");
    public static final ReplacementReason WRONGDESCRIPTION = valueOf("WrongDescription");
    public static final ReplacementReason LOSTINTRANSIT = valueOf("LostInTransit");
    public static final ReplacementReason MISPICKWRONGITEMDELIVERED = valueOf("MispickWrongItemDelivered");
    public static final ReplacementReason MISPICKITEMMISSING = valueOf("MispickItemMissing");
    public static final ReplacementReason REFUSED = valueOf("Refused");
    public static final ReplacementReason GOODWILL = valueOf("GoodWill");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ReplacementReason(String code)
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
        return "ReplacementReason";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ReplacementReason valueOf(String code)
    {
        String key = code.toLowerCase();
        ReplacementReason result = cache.get(key);
        if(result == null)
        {
            ReplacementReason newValue = new ReplacementReason(code);
            ReplacementReason previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

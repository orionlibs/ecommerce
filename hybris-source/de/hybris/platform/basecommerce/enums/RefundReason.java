package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RefundReason implements HybrisEnumValue
{
    public static final String _TYPECODE = "RefundReason";
    public static final String SIMPLE_CLASSNAME = "RefundReason";
    private static final ConcurrentMap<String, RefundReason> cache = new ConcurrentHashMap<>();
    public static final RefundReason DAMAGEDINTRANSIT = valueOf("DamagedInTransit");
    public static final RefundReason LATEDELIVERY = valueOf("LateDelivery");
    public static final RefundReason PRICEMATCH = valueOf("PriceMatch");
    public static final RefundReason LOSTINTRANSIT = valueOf("LostInTransit");
    public static final RefundReason MANUFACTURINGFAULT = valueOf("ManufacturingFault");
    public static final RefundReason WRONGDESCRIPTION = valueOf("WrongDescription");
    public static final RefundReason MISSEDLINKDEAL = valueOf("MissedLinkDeal");
    public static final RefundReason MISPICKWRONGITEMDELIVERED = valueOf("MispickWrongItemDelivered");
    public static final RefundReason MISPICKITEMMISSING = valueOf("MispickItemMissing");
    public static final RefundReason SITEERROR = valueOf("SiteError");
    public static final RefundReason GOODWILL = valueOf("GoodWill");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private RefundReason(String code)
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
        return "RefundReason";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static RefundReason valueOf(String code)
    {
        String key = code.toLowerCase();
        RefundReason result = cache.get(key);
        if(result == null)
        {
            RefundReason newValue = new RefundReason(code);
            RefundReason previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsignmentStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "ConsignmentStatus";
    public static final String SIMPLE_CLASSNAME = "ConsignmentStatus";
    private static final ConcurrentMap<String, ConsignmentStatus> cache = new ConcurrentHashMap<>();
    public static final ConsignmentStatus BACK_ORDERED = valueOf("BACK_ORDERED");
    public static final ConsignmentStatus READY_FOR_PICKUP = valueOf("READY_FOR_PICKUP");
    public static final ConsignmentStatus WAITING = valueOf("WAITING");
    public static final ConsignmentStatus INVOICED = valueOf("INVOICED");
    public static final ConsignmentStatus PICKPACK = valueOf("PICKPACK");
    public static final ConsignmentStatus PICKUP_COMPLETE = valueOf("PICKUP_COMPLETE");
    public static final ConsignmentStatus BEING_PROCESSED = valueOf("BEING_PROCESSED");
    public static final ConsignmentStatus PAYMENT_NOT_CAPTURED = valueOf("PAYMENT_NOT_CAPTURED");
    public static final ConsignmentStatus READY = valueOf("READY");
    public static final ConsignmentStatus SHIPPED = valueOf("SHIPPED");
    public static final ConsignmentStatus TAX_NOT_COMMITTED = valueOf("TAX_NOT_COMMITTED");
    public static final ConsignmentStatus CANCELLED = valueOf("CANCELLED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ConsignmentStatus(String code)
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
        return "ConsignmentStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ConsignmentStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        ConsignmentStatus result = cache.get(key);
        if(result == null)
        {
            ConsignmentStatus newValue = new ConsignmentStatus(code);
            ConsignmentStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

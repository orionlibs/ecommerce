package de.hybris.platform.sap.sapmodel.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsignmentEntryStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "ConsignmentEntryStatus";
    public static final String SIMPLE_CLASSNAME = "ConsignmentEntryStatus";
    private static final ConcurrentMap<String, ConsignmentEntryStatus> cache = new ConcurrentHashMap<>();
    public static final ConsignmentEntryStatus WAITING = valueOf("WAITING");
    public static final ConsignmentEntryStatus PICKPACK = valueOf("PICKPACK");
    public static final ConsignmentEntryStatus READY = valueOf("READY");
    public static final ConsignmentEntryStatus SHIPPED = valueOf("SHIPPED");
    public static final ConsignmentEntryStatus CANCELLED = valueOf("CANCELLED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ConsignmentEntryStatus(String code)
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
        return "ConsignmentEntryStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ConsignmentEntryStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        ConsignmentEntryStatus result = cache.get(key);
        if(result == null)
        {
            ConsignmentEntryStatus newValue = new ConsignmentEntryStatus(code);
            ConsignmentEntryStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.sap.sapmodel.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SapSystemType implements HybrisEnumValue
{
    public static final String _TYPECODE = "SapSystemType";
    public static final String SIMPLE_CLASSNAME = "SapSystemType";
    private static final ConcurrentMap<String, SapSystemType> cache = new ConcurrentHashMap<>();
    public static final SapSystemType SAP_ERP = valueOf("SAP_ERP");
    public static final SapSystemType SAP_S4HANA = valueOf("SAP_S4HANA");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private SapSystemType(String code)
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
        return "SapSystemType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static SapSystemType valueOf(String code)
    {
        String key = code.toLowerCase();
        SapSystemType result = cache.get(key);
        if(result == null)
        {
            SapSystemType newValue = new SapSystemType(code);
            SapSystemType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

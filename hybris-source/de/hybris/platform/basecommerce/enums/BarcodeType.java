package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BarcodeType implements HybrisEnumValue
{
    public static final String _TYPECODE = "BarcodeType";
    public static final String SIMPLE_CLASSNAME = "BarcodeType";
    private static final ConcurrentMap<String, BarcodeType> cache = new ConcurrentHashMap<>();
    public static final BarcodeType QR = valueOf("QR");
    public static final BarcodeType DATAMATRIX = valueOf("DATAMATRIX");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private BarcodeType(String code)
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
        return "BarcodeType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static BarcodeType valueOf(String code)
    {
        String key = code.toLowerCase();
        BarcodeType result = cache.get(key);
        if(result == null)
        {
            BarcodeType newValue = new BarcodeType(code);
            BarcodeType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

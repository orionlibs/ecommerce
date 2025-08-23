package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MerchantCheckType implements HybrisEnumValue
{
    public static final String _TYPECODE = "MerchantCheckType";
    public static final String SIMPLE_CLASSNAME = "MerchantCheckType";
    private static final ConcurrentMap<String, MerchantCheckType> cache = new ConcurrentHashMap<>();
    public static final MerchantCheckType CREDITLIMIT = valueOf("CREDITLIMIT");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private MerchantCheckType(String code)
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
        return "MerchantCheckType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static MerchantCheckType valueOf(String code)
    {
        String key = code.toLowerCase();
        MerchantCheckType result = cache.get(key);
        if(result == null)
        {
            MerchantCheckType newValue = new MerchantCheckType(code);
            MerchantCheckType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

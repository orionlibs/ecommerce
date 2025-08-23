package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MerchantCheckStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "MerchantCheckStatus";
    public static final String SIMPLE_CLASSNAME = "MerchantCheckStatus";
    private static final ConcurrentMap<String, MerchantCheckStatus> cache = new ConcurrentHashMap<>();
    public static final MerchantCheckStatus APPROVED = valueOf("APPROVED");
    public static final MerchantCheckStatus REJECTED = valueOf("REJECTED");
    public static final MerchantCheckStatus OPEN = valueOf("OPEN");
    public static final MerchantCheckStatus ERROR = valueOf("ERROR");
    public static final MerchantCheckStatus FAILURE = valueOf("FAILURE");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private MerchantCheckStatus(String code)
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
        return "MerchantCheckStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static MerchantCheckStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        MerchantCheckStatus result = cache.get(key);
        if(result == null)
        {
            MerchantCheckStatus newValue = new MerchantCheckStatus(code);
            MerchantCheckStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FraudStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "FraudStatus";
    public static final String SIMPLE_CLASSNAME = "FraudStatus";
    private static final ConcurrentMap<String, FraudStatus> cache = new ConcurrentHashMap<>();
    public static final FraudStatus OK = valueOf("OK");
    public static final FraudStatus CHECK = valueOf("CHECK");
    public static final FraudStatus FRAUD = valueOf("FRAUD");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private FraudStatus(String code)
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
        return "FraudStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static FraudStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        FraudStatus result = cache.get(key);
        if(result == null)
        {
            FraudStatus newValue = new FraudStatus(code);
            FraudStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

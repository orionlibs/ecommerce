package de.hybris.platform.integrationservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IntegrationRequestStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "IntegrationRequestStatus";
    public static final String SIMPLE_CLASSNAME = "IntegrationRequestStatus";
    private static final ConcurrentMap<String, IntegrationRequestStatus> cache = new ConcurrentHashMap<>();
    public static final IntegrationRequestStatus SUCCESS = valueOf("SUCCESS");
    public static final IntegrationRequestStatus ERROR = valueOf("ERROR");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private IntegrationRequestStatus(String code)
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
        return "IntegrationRequestStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static IntegrationRequestStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        IntegrationRequestStatus result = cache.get(key);
        if(result == null)
        {
            IntegrationRequestStatus newValue = new IntegrationRequestStatus(code);
            IntegrationRequestStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

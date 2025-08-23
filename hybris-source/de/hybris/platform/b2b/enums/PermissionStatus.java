package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PermissionStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "PermissionStatus";
    public static final String SIMPLE_CLASSNAME = "PermissionStatus";
    private static final ConcurrentMap<String, PermissionStatus> cache = new ConcurrentHashMap<>();
    public static final PermissionStatus APPROVED = valueOf("APPROVED");
    public static final PermissionStatus REJECTED = valueOf("REJECTED");
    public static final PermissionStatus PENDING_APPROVAL = valueOf("PENDING_APPROVAL");
    public static final PermissionStatus OPEN = valueOf("OPEN");
    public static final PermissionStatus ERROR = valueOf("ERROR");
    public static final PermissionStatus FAILURE = valueOf("FAILURE");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private PermissionStatus(String code)
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
        return "PermissionStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static PermissionStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        PermissionStatus result = cache.get(key);
        if(result == null)
        {
            PermissionStatus newValue = new PermissionStatus(code);
            PermissionStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

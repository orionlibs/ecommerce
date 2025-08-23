package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class B2BPermissionTypeEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "B2BPermissionTypeEnum";
    public static final String SIMPLE_CLASSNAME = "B2BPermissionTypeEnum";
    private static final ConcurrentMap<String, B2BPermissionTypeEnum> cache = new ConcurrentHashMap<>();
    public static final B2BPermissionTypeEnum B2BORDERTHRESHOLDPERMISSION = valueOf("B2BOrderThresholdPermission");
    public static final B2BPermissionTypeEnum B2BORDERTHRESHOLDTIMESPANPERMISSION = valueOf("B2BOrderThresholdTimespanPermission");
    public static final B2BPermissionTypeEnum B2BBUDGETEXCEEDEDPERMISSION = valueOf("B2BBudgetExceededPermission");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private B2BPermissionTypeEnum(String code)
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
        return "B2BPermissionTypeEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static B2BPermissionTypeEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        B2BPermissionTypeEnum result = cache.get(key);
        if(result == null)
        {
            B2BPermissionTypeEnum newValue = new B2BPermissionTypeEnum(code);
            B2BPermissionTypeEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

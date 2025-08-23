package de.hybris.platform.acceleratorservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SiteMapPageEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "SiteMapPageEnum";
    public static final String SIMPLE_CLASSNAME = "SiteMapPageEnum";
    private static final ConcurrentMap<String, SiteMapPageEnum> cache = new ConcurrentHashMap<>();
    public static final SiteMapPageEnum HOMEPAGE = valueOf("Homepage");
    public static final SiteMapPageEnum PRODUCT = valueOf("Product");
    public static final SiteMapPageEnum CATEGORY = valueOf("Category");
    public static final SiteMapPageEnum CATEGORYLANDING = valueOf("CategoryLanding");
    public static final SiteMapPageEnum STORE = valueOf("Store");
    public static final SiteMapPageEnum CONTENT = valueOf("Content");
    public static final SiteMapPageEnum CUSTOM = valueOf("Custom");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private SiteMapPageEnum(String code)
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
        return "SiteMapPageEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static SiteMapPageEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        SiteMapPageEnum result = cache.get(key);
        if(result == null)
        {
            SiteMapPageEnum newValue = new SiteMapPageEnum(code);
            SiteMapPageEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

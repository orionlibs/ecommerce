package de.hybris.platform.solrfacetsearch.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SolrPropertiesTypes implements HybrisEnumValue
{
    public static final String _TYPECODE = "SolrPropertiesTypes";
    public static final String SIMPLE_CLASSNAME = "SolrPropertiesTypes";
    private static final ConcurrentMap<String, SolrPropertiesTypes> cache = new ConcurrentHashMap<>();
    public static final SolrPropertiesTypes BOOLEAN = valueOf("boolean");
    public static final SolrPropertiesTypes INT = valueOf("int");
    public static final SolrPropertiesTypes STRING = valueOf("string");
    public static final SolrPropertiesTypes SORTABLETEXT = valueOf("sortabletext");
    public static final SolrPropertiesTypes TEXT = valueOf("text");
    public static final SolrPropertiesTypes FLOAT = valueOf("float");
    public static final SolrPropertiesTypes DOUBLE = valueOf("double");
    public static final SolrPropertiesTypes DATE = valueOf("date");
    public static final SolrPropertiesTypes LONG = valueOf("long");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private SolrPropertiesTypes(String code)
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
        return "SolrPropertiesTypes";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static SolrPropertiesTypes valueOf(String code)
    {
        String key = code.toLowerCase();
        SolrPropertiesTypes result = cache.get(key);
        if(result == null)
        {
            SolrPropertiesTypes newValue = new SolrPropertiesTypes(code);
            SolrPropertiesTypes previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

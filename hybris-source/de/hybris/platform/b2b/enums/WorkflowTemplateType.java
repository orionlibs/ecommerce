package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WorkflowTemplateType implements HybrisEnumValue
{
    public static final String _TYPECODE = "WorkflowTemplateType";
    public static final String SIMPLE_CLASSNAME = "WorkflowTemplateType";
    private static final ConcurrentMap<String, WorkflowTemplateType> cache = new ConcurrentHashMap<>();
    public static final WorkflowTemplateType ORDER_APPROVAL = valueOf("ORDER_APPROVAL");
    public static final WorkflowTemplateType CREDIT_LIMIT_ALERT = valueOf("CREDIT_LIMIT_ALERT");
    public static final WorkflowTemplateType MERCHANT_CHECK = valueOf("MERCHANT_CHECK");
    public static final WorkflowTemplateType SALES_QUOTES = valueOf("SALES_QUOTES");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private WorkflowTemplateType(String code)
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
        return "WorkflowTemplateType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static WorkflowTemplateType valueOf(String code)
    {
        String key = code.toLowerCase();
        WorkflowTemplateType result = cache.get(key);
        if(result == null)
        {
            WorkflowTemplateType newValue = new WorkflowTemplateType(code);
            WorkflowTemplateType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

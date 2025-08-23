package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReturnStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "ReturnStatus";
    public static final String SIMPLE_CLASSNAME = "ReturnStatus";
    private static final ConcurrentMap<String, ReturnStatus> cache = new ConcurrentHashMap<>();
    public static final ReturnStatus CANCELED = valueOf("CANCELED");
    public static final ReturnStatus WAIT = valueOf("WAIT");
    public static final ReturnStatus RECEIVED = valueOf("RECEIVED");
    public static final ReturnStatus APPROVAL_PENDING = valueOf("APPROVAL_PENDING");
    public static final ReturnStatus APPROVING = valueOf("APPROVING");
    public static final ReturnStatus RECEIVING = valueOf("RECEIVING");
    public static final ReturnStatus CANCELLING = valueOf("CANCELLING");
    public static final ReturnStatus PAYMENT_REVERSED = valueOf("PAYMENT_REVERSED");
    public static final ReturnStatus PAYMENT_REVERSAL_FAILED = valueOf("PAYMENT_REVERSAL_FAILED");
    public static final ReturnStatus TAX_REVERSED = valueOf("TAX_REVERSED");
    public static final ReturnStatus TAX_REVERSAL_FAILED = valueOf("TAX_REVERSAL_FAILED");
    public static final ReturnStatus COMPLETED = valueOf("COMPLETED");
    public static final ReturnStatus REVERSING_PAYMENT = valueOf("REVERSING_PAYMENT");
    public static final ReturnStatus REVERSING_TAX = valueOf("REVERSING_TAX");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ReturnStatus(String code)
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
        return "ReturnStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ReturnStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        ReturnStatus result = cache.get(key);
        if(result == null)
        {
            ReturnStatus newValue = new ReturnStatus(code);
            ReturnStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

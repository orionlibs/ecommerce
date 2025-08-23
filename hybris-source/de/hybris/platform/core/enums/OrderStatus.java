package de.hybris.platform.core.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OrderStatus implements HybrisEnumValue
{
    public static final String _TYPECODE = "OrderStatus";
    public static final String SIMPLE_CLASSNAME = "OrderStatus";
    private static final ConcurrentMap<String, OrderStatus> cache = new ConcurrentHashMap<>();
    public static final OrderStatus CANCELLING = valueOf("CANCELLING");
    public static final OrderStatus CHECKED_VALID = valueOf("CHECKED_VALID");
    public static final OrderStatus CREATED = valueOf("CREATED");
    public static final OrderStatus OPEN = valueOf("OPEN");
    public static final OrderStatus CHECKED_INVALID = valueOf("CHECKED_INVALID");
    public static final OrderStatus ON_VALIDATION = valueOf("ON_VALIDATION");
    public static final OrderStatus PENDING_APPROVAL = valueOf("PENDING_APPROVAL");
    public static final OrderStatus SUSPENDED = valueOf("SUSPENDED");
    public static final OrderStatus COMPLETED = valueOf("COMPLETED");
    public static final OrderStatus PAYMENT_AUTHORIZED = valueOf("PAYMENT_AUTHORIZED");
    public static final OrderStatus PENDING_APPROVAL_FROM_MERCHANT = valueOf("PENDING_APPROVAL_FROM_MERCHANT");
    public static final OrderStatus CANCELLED = valueOf("CANCELLED");
    public static final OrderStatus PAYMENT_NOT_AUTHORIZED = valueOf("PAYMENT_NOT_AUTHORIZED");
    public static final OrderStatus PENDING_QUOTE = valueOf("PENDING_QUOTE");
    public static final OrderStatus APPROVED_QUOTE = valueOf("APPROVED_QUOTE");
    public static final OrderStatus PAYMENT_AMOUNT_RESERVED = valueOf("PAYMENT_AMOUNT_RESERVED");
    public static final OrderStatus PAYMENT_AMOUNT_NOT_RESERVED = valueOf("PAYMENT_AMOUNT_NOT_RESERVED");
    public static final OrderStatus REJECTED_QUOTE = valueOf("REJECTED_QUOTE");
    public static final OrderStatus APPROVED = valueOf("APPROVED");
    public static final OrderStatus PAYMENT_CAPTURED = valueOf("PAYMENT_CAPTURED");
    public static final OrderStatus PAYMENT_NOT_CAPTURED = valueOf("PAYMENT_NOT_CAPTURED");
    public static final OrderStatus REJECTED = valueOf("REJECTED");
    public static final OrderStatus APPROVED_BY_MERCHANT = valueOf("APPROVED_BY_MERCHANT");
    public static final OrderStatus FRAUD_CHECKED = valueOf("FRAUD_CHECKED");
    public static final OrderStatus ORDER_SPLIT = valueOf("ORDER_SPLIT");
    public static final OrderStatus REJECTED_BY_MERCHANT = valueOf("REJECTED_BY_MERCHANT");
    public static final OrderStatus ASSIGNED_TO_ADMIN = valueOf("ASSIGNED_TO_ADMIN");
    public static final OrderStatus PROCESSING_ERROR = valueOf("PROCESSING_ERROR");
    public static final OrderStatus B2B_PROCESSING_ERROR = valueOf("B2B_PROCESSING_ERROR");
    public static final OrderStatus WAIT_FRAUD_MANUAL_CHECK = valueOf("WAIT_FRAUD_MANUAL_CHECK");
    public static final OrderStatus PAYMENT_NOT_VOIDED = valueOf("PAYMENT_NOT_VOIDED");
    public static final OrderStatus TAX_NOT_VOIDED = valueOf("TAX_NOT_VOIDED");
    public static final OrderStatus TAX_NOT_COMMITTED = valueOf("TAX_NOT_COMMITTED");
    public static final OrderStatus TAX_NOT_REQUOTED = valueOf("TAX_NOT_REQUOTED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private OrderStatus(String code)
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
        return "OrderStatus";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static OrderStatus valueOf(String code)
    {
        String key = code.toLowerCase();
        OrderStatus result = cache.get(key);
        if(result == null)
        {
            OrderStatus newValue = new OrderStatus(code);
            OrderStatus previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}

package de.hybris.platform.payment.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum PaymentTransactionType implements HybrisEnumValue
{
    AUTHORIZATION("AUTHORIZATION"),
    REVIEW_DECISION("REVIEW_DECISION"),
    CAPTURE("CAPTURE"),
    PARTIAL_CAPTURE("PARTIAL_CAPTURE"),
    REFUND_FOLLOW_ON("REFUND_FOLLOW_ON"),
    REFUND_STANDALONE("REFUND_STANDALONE"),
    CANCEL("CANCEL"),
    CREATE_SUBSCRIPTION("CREATE_SUBSCRIPTION"),
    UPDATE_SUBSCRIPTION("UPDATE_SUBSCRIPTION"),
    GET_SUBSCRIPTION_DATA("GET_SUBSCRIPTION_DATA"),
    DELETE_SUBSCRIPTION("DELETE_SUBSCRIPTION");
    public static final String _TYPECODE = "PaymentTransactionType";
    public static final String SIMPLE_CLASSNAME = "PaymentTransactionType";
    private final String code;


    PaymentTransactionType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "PaymentTransactionType";
    }
}

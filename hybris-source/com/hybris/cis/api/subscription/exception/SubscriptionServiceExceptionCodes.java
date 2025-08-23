package com.hybris.cis.api.subscription.exception;

import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;

public enum SubscriptionServiceExceptionCodes implements StandardServiceExceptionCode
{
    SBG_NO_CONNECTION(7000, "A connection could not be made with the subscription billing gateway.  Please try again in a moment."),
    PARAMETERS_SBG_INVALID(7001, "Invalid parameters"),
    PARAMETERS_REF_INVALID(7002, "Invalid parameters referenced"),
    VALIDATION_AVS_FAILED(7003, "Validation failed: AVS policy evaluation failed."),
    VALIDATION_CVN_FAILED(7004, "Validation failed: CVN policy evaluation failed."),
    VALIDATION_AVS_CVN_FAILED(7005, "Validation failed: AVS and CVN policy evaluation failed."),
    VALIDATION_AVS_CVN_NOT_POSSIBLE(7006, "Validation failed: unable to perform AVS and CVN policy evaluation."),
    PAYMENT_METHOD_NOT_IMPLEMENTED(7007, "Payment method not implemented."),
    PROVIDER_AUTHENTICATION_FAILED(7008, "Your request could not be authenticated."),
    PROVIDER_INTERNAL_ERROR(7009, "The subscription billing provider encountered an internal error. Please try again in a moment."),
    PROVIDER_UNABLE_TO_CONNECT(7010, "A connection could not be made with the subscription billing provider.  Please try again in a moment."),
    ACCOUNT_ID_MISSING(7011, "The account id is missing from the request"),
    ACCOUNT_ID_INVALID(7012, "The account id is invalid"),
    PAYMENT_METHOD_ID_MISSING(7013, "The payment method id is missing from the request"),
    PAYMENT_METHOD_ID_INVALID(7014, "The payment method id is invalid"),
    SUBSCRIPTION_ID_MISSING(7015, "The subscription id is missing from the request"),
    SUBSCRIPTION_ID_INVALID(7016, "The subscription id is invalid"),
    SUBSCRIPTION_CHANGESTATE_NEWSTATE_MISSING(7017, "The new subscription state is missing from the request"),
    SESSION_TOKEN_MISSING(7018, "The session token is missing from the request"),
    SESSION_TOKEN_INVALID(7019, "The session token is invalid"),
    MANDATORY_PARAMETER_MISSING(7020, "A mandatory parameter is missing from the request"),
    USAGE_CHARGE_NAME_UNKNOWN(7021, "The usage charge name is not found");
    private final int code;
    private final String message;


    SubscriptionServiceExceptionCodes(int code, String message)
    {
        this.code = code;
        this.message = message;
    }


    public int getCode()
    {
        return this.code;
    }


    public String getMessage()
    {
        return this.message;
    }


    public String toString()
    {
        return "" + this.code + " - " + this.code;
    }
}

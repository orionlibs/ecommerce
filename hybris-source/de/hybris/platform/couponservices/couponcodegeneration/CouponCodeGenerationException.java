package de.hybris.platform.couponservices.couponcodegeneration;

public class CouponCodeGenerationException extends RuntimeException
{
    public static final int DEFAULT_ERROR_CODE = 0;
    public static final int ERROR_MAXIMUM_COUPON_CODES_GENERATED = 500;
    private final int errorCode;


    public CouponCodeGenerationException(String message, int errorCode, Throwable cause)
    {
        super(message, cause);
        this.errorCode = errorCode;
    }


    public CouponCodeGenerationException(String message, int errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }


    public int getErrorCode()
    {
        return this.errorCode;
    }
}

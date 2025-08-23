package de.hybris.platform.couponservices.constants;

public final class CouponServicesConstants extends GeneratedCouponServicesConstants
{
    public static final String EXTENSIONNAME = "couponservices";
    public static final String COUPON_CODE_GENERATION_ALGORITHM_PROPERTY = "couponservices.code.generation.signature.algorithm";
    public static final String COUPON_CODE_GENERATION_ALGORITHM_DEFAULT_VALUE = "AES";
    public static final String COUPON_CODE_GENERATION_KEY_SIZE_PROPERTY = "couponservices.code.generation.signature.keysize";
    public static final int COUPON_CODE_GENERATION_KEY_SIZE_DEFAULT_VALUE = 256;
    public static final String COUPON_CODE_GENERATION_GLOBAL_CHARACTERSET_PROPERTY = "couponservices.code.generation.global.characterset";
    public static final String COUPON_CODE_GENERATION_GLOBAL_CHARACTERSET_DEFAULT_VALUE = "123456789ABCDEFGHKLMNPRSTWXYZ";
    public static final String COUPON_CODE_GENERATION_ALPHABET_LENGTH_PROPERTY = "couponservices.code.generation.alphabet.length";
    public static final String COUPON_CODE_GENERATION_MEDIA_FOLDER_QUALIFIER = "couponservices.code.generation.media.folder.qualifier";
    public static final String COUPON_CODE_GENERATION_MEDIA_FOLDER_QUALIFIER_DEFAULT_VALUE = "couponcodes";
    public static final int COUPON_CODE_GENERATION_ALPHABET_LENGTH_DEFAULT_VALUE = 16;
    public static final String COUPON_CODE_GENERATION_PICK_ALPHABET_USING_SECURERANDOM_PROPERTY = "couponservices.code.generation.pick.alphabet.using.securerandom";
    public static final String COUPON_CODE_GENERATION_PREFIX_REGEX_PROPERTY = "couponservices.code.generation.prefix.regexp";
    public static final String COUPON_CODE_GENERATION_PREFIX_REGEX_DEFAULT_VALUE = "[A-Za-z0-9]+";
    public static final String COUPON_CODE_GENERATION_ALLOW_MULTIBYTE_CHARACTERS_PROPERTY = "couponservices.code.generation.global.characterset.allow.multibyte.characters";
    public static final String COUPON_CODE_GENERATION_SIGNATURE_ALGORITHM_ALLOW_NON_AES_PROPERTY = "couponservices.code.generation.signature.algorithm.allow.non-aes";
    public static final String COUPON_CODE_GENERATION_ALPHABET_ALLOW_VARIABLE_LENGTH_PROPERTY = "couponservices.code.generation.alphabet.allow.variable.length";
    public static final String COUPON_CODE_INVALID_ERROR_CODE = "coupon.invalid.code.provided";
    public static final String COUPON_CODE_ALREADY_REDEEMED_ERROR_CODE = "coupon.already.redeemed";
    public static final String COUPON_ORDER_RECALCULATION_ERROR_CODE = "coupon.order.recalculation.error";
    public static final String COUPON_CODE_EXPIRED_ERROR_CODE = "coupon.not.active.expired";
    public static final String COUPON_CODE_ALREADY_EXISTS_ERROR_CODE = "coupon.already.exists.cart";
    public static final String COUPON_GENERAL_ERROR_CODE = "coupon.general.error";
    public static final String COUPON_REDEMPTION_VALIDATION_PRO = "couponservices.coupon.redemption.validation";
    public static final String COUPON_IDS = "couponIds";
}

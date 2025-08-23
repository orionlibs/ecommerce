package de.hybris.platform.cache;

import de.hybris.platform.core.PK;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class InvalidationKey
{
    public static final int PK_POSITION = 3;
    private static final int HJMP_POSITION = 0;
    private static final int ENTITY_POSITION = 1;
    private static final int ADDITIONAL_DATA_POSITION = 4;
    private static final int BASE_KEY_LENGTH = 4;
    private static final int EXTENDED_KEY_LENGTH = 5;


    public static Object[] entityArrayKey(PK pk)
    {
        return entityArrayKey(pk, null);
    }


    public static Object[] entityArrayKey(PK pk, AdditionalInvalidationData additionalInvalidationData)
    {
        Objects.requireNonNull(pk);
        if(additionalInvalidationData == null)
        {
            return new Object[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, pk.getTypeCodeAsString(), pk};
        }
        return new Object[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, pk.getTypeCodeAsString(), pk, additionalInvalidationData};
    }


    public static PK requireEntityArrayKey(Object[] key)
    {
        if(!isEntityArrayKey(key))
        {
            String msg = "Unsupported key. Expected entity key but `" + Arrays.toString(key) + "` has been received.";
            throw new IllegalArgumentException(msg);
        }
        return (PK)key[3];
    }


    public static AdditionalInvalidationData requireAdditionalInvalidationData(Object[] key)
    {
        if(!containsAdditionalInvalidationData(key))
        {
            throw new IllegalArgumentException("Unsupported key. Expected entity key with additional data but `" +
                            Arrays.toString(key) + "` has been received.");
        }
        return (AdditionalInvalidationData)key[4];
    }


    public static Object[] setAdditionalData(Object[] key, AdditionalInvalidationData data)
    {
        requireEntityArrayKey(key);
        Objects.requireNonNull(data);
        Object[] resultKey = (key.length == 5) ? key : Arrays.<Object>copyOf(key, 5);
        resultKey[4] = data;
        return resultKey;
    }


    public static Object[] asBaseEntityArrayKey(Object[] key)
    {
        requireEntityArrayKey(key);
        if(key.length == 4)
        {
            return key;
        }
        return Arrays.copyOf(key, 4);
    }


    public static Optional<PkWithAdditionalData> extractPkWithAdditionalData(Object[] key)
    {
        return Optional.ofNullable(getPkWithAdditionalData(key));
    }


    public static boolean containsAdditionalInvalidationData(Object[] key)
    {
        return (isEntityArrayKey(key) && key.length == 5);
    }


    public static boolean isEntityArrayKey(Object[] key)
    {
        if(!startsWithBaseEntityArrayKey(key) || key.length > 5)
        {
            return false;
        }
        if(key.length == 4)
        {
            return true;
        }
        return key[4] instanceof AdditionalInvalidationData;
    }


    public static boolean startsWithBaseEntityArrayKey(Object[] key)
    {
        return (isValidKeyLength(key) && Cache.CACHEKEY_HJMP.equals(key[0]) && Cache.CACHEKEY_ENTITY
                        .equals(key[1]) && key[3] instanceof PK);
    }


    private static boolean isValidKeyLength(Object[] key)
    {
        return (key != null && key.length >= 4);
    }


    private static PkWithAdditionalData getPkWithAdditionalData(Object[] key)
    {
        if(!isEntityArrayKey(key))
        {
            return null;
        }
        if(key.length == 4)
        {
            return new PkWithAdditionalData((PK)key[3]);
        }
        return new PkWithAdditionalData((PK)key[3], (AdditionalInvalidationData)key[4]);
    }
}

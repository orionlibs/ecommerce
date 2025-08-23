package de.hybris.platform.util.typesystem;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PlatformStringUtils
{
    private static final int MAX_TYPE_CODE_ID = 65536;
    private static final Map<String, String> CACHED_LOW_NAMES = new ConcurrentHashMap<>(4096, 0.75F, 16);
    private static final String[] CACHED_TYPE_STR_CODES = new String[65536];


    public static String valueOf(int value)
    {
        String convertedTypeCode = CACHED_TYPE_STR_CODES[value];
        if(convertedTypeCode == null)
        {
            convertedTypeCode = String.valueOf(value).intern();
            CACHED_TYPE_STR_CODES[value] = convertedTypeCode;
        }
        return convertedTypeCode;
    }


    public static String valueOf(Integer value)
    {
        Preconditions.checkNotNull(value);
        String convertedTypeCode = CACHED_TYPE_STR_CODES[value.intValue()];
        if(convertedTypeCode == null)
        {
            convertedTypeCode = String.valueOf(value).intern();
            CACHED_TYPE_STR_CODES[value.intValue()] = convertedTypeCode;
        }
        return convertedTypeCode;
    }


    public static String toLowerCaseCached(String key)
    {
        String value = CACHED_LOW_NAMES.get(key);
        if(value == null)
        {
            value = key.toLowerCase(LocaleHelper.getPersistenceLocale()).intern();
            CACHED_LOW_NAMES.put(key.intern(), value);
        }
        return value;
    }
}

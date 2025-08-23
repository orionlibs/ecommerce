package de.hybris.platform.servicelayer.i18n.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class I18NUtils
{
    private static final Map<String, String> FORMER_TO_ACTUAL_ISO_CODE_MAPPING = (Map<String, String>)ImmutableMap.of("iw", "he", "ji", "yi", "in", "id");


    public static boolean isFormerISOCode(String code)
    {
        return FORMER_TO_ACTUAL_ISO_CODE_MAPPING.containsKey(code.toLowerCase());
    }


    public static String mapFormerISOCodeToActual(String formerCode)
    {
        Preconditions.checkArgument((formerCode != null));
        String formerTwoLetterCode = extractTwoLetterCode(formerCode);
        String actualTwoLetterCode = FORMER_TO_ACTUAL_ISO_CODE_MAPPING.get(formerTwoLetterCode);
        if(actualTwoLetterCode == null)
        {
            actualTwoLetterCode = formerTwoLetterCode;
        }
        if(formerCode.length() > 2)
        {
            return actualTwoLetterCode + actualTwoLetterCode;
        }
        return actualTwoLetterCode;
    }


    private static String extractTwoLetterCode(String code)
    {
        if(code.length() > 2)
        {
            return code.substring(0, 2).toLowerCase();
        }
        return code.toLowerCase();
    }
}

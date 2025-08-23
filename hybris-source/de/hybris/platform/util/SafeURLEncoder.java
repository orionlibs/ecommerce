package de.hybris.platform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.validator.GenericValidator;

public class SafeURLEncoder
{
    private static final String URL_ENC_SCHEME = "UTF-8";


    public static String encode(String input)
    {
        if(GenericValidator.isBlankOrNull(input))
        {
            return input;
        }
        try
        {
            return URLEncoder.encode(input, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException("Broken VM does not support UTF-8");
        }
    }
}

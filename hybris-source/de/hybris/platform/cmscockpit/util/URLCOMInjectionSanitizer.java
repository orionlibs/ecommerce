package de.hybris.platform.cmscockpit.util;

import com.google.common.base.Preconditions;
import java.util.Map;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class URLCOMInjectionSanitizer
{
    private static Map<String, String> preProcessCharsToSanitize;
    private static Map<String, String> postProcessCharsToSanitize;


    public URLCOMInjectionSanitizer(Map<String, String> preProcessCharsToSanitize, Map<String, String> postProcessCharsToSanitize)
    {
        URLCOMInjectionSanitizer.preProcessCharsToSanitize = preProcessCharsToSanitize;
        URLCOMInjectionSanitizer.postProcessCharsToSanitize = postProcessCharsToSanitize;
    }


    public static String sanitize(String url) throws NullPointerException
    {
        Preconditions.checkNotNull(preProcessCharsToSanitize, ClassUtils.getShortClassName(URLCOMInjectionSanitizer.class) + " not instantiated or instantiated with null preProcessCharsToSanitize map.");
        Preconditions.checkNotNull(postProcessCharsToSanitize, ClassUtils.getShortClassName(URLCOMInjectionSanitizer.class) + " not instantiated or instantiated with null postProcessCharsToSanitize map.");
        String urlReplaceSpecialChars = url;
        for(Map.Entry<String, String> entry : preProcessCharsToSanitize.entrySet())
        {
            urlReplaceSpecialChars = StringUtils.replace(urlReplaceSpecialChars, entry.getKey(), entry.getValue());
        }
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(urlReplaceSpecialChars);
        UriComponents urlSanitized = urlBuilder.build().encode();
        urlReplaceSpecialChars = urlSanitized.toUriString();
        for(Map.Entry<String, String> entry : postProcessCharsToSanitize.entrySet())
        {
            urlReplaceSpecialChars = StringUtils.replace(urlReplaceSpecialChars, entry.getKey(), entry.getValue());
        }
        return urlReplaceSpecialChars;
    }
}

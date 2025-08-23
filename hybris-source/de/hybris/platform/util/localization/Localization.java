package de.hybris.platform.util.localization;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Localization
{
    public static String getLocalizedString(String key, Object[] arguments)
    {
        String value = getLocalizedString(key);
        if(value != null && arguments != null && arguments.length != 0)
        {
            Locale locale = JaloSession.getCurrentSession().getSessionContext().getLocale();
            MessageFormat messageFormat = new MessageFormat(value, locale);
            return messageFormat.format(arguments, new StringBuffer(), (FieldPosition)null).toString();
        }
        return value;
    }


    public static String getLocalizedString(String key)
    {
        Map<Language, Properties> localizations = TypeLocalization.getInstance().getLocalizations();
        Properties props = null;
        Language[] langs = getLanguagePath(JaloSession.getCurrentSession().getSessionContext().getLanguage());
        for(int i = 0; i < langs.length; i++)
        {
            props = localizations.get(langs[i]);
            if(props != null)
            {
                String value = (String)props.get(key.toLowerCase());
                if(value != null)
                {
                    return value;
                }
            }
        }
        return key;
    }


    public static Map<Language, String> getLocalizedMap(String key)
    {
        Map<Language, Properties> locMap = TypeLocalization.getInstance().getLocalizations();
        Map<Language, String> map = new HashMap<>();
        for(Map.Entry<Language, Properties> entry : locMap.entrySet())
        {
            Language l = entry.getKey();
            String name = (String)((Properties)entry.getValue()).get(key.toLowerCase());
            map.put(l, name);
        }
        return map;
    }


    private static Language[] getLanguagePath(Language lang)
    {
        List<Language> lst = new ArrayList<>();
        if(lang != null)
        {
            lst.add(lang);
            for(Language fbl : lang.getFallbackLanguages())
            {
                if(fbl != null && !lst.contains(fbl))
                {
                    lst.add(fbl);
                }
            }
        }
        return lst.<Language>toArray(new Language[lst.size()]);
    }
}

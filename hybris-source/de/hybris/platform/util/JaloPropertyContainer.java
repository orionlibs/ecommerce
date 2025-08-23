package de.hybris.platform.util;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;

public interface JaloPropertyContainer
{
    void setProperty(String paramString, Object paramObject);


    void setLocalizedProperty(String paramString, Object paramObject);


    void setLocalizedProperty(SessionContext paramSessionContext, String paramString, Object paramObject);


    void setAllLocalizedProperties(String paramString, Map<Language, Object> paramMap);
}

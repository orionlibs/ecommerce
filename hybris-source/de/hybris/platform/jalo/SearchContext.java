package de.hybris.platform.jalo;

import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;

public interface SearchContext extends Cloneable
{
    public static final int RANGE_MAX = -1;


    void setRange(int paramInt1, int paramInt2);


    int getRangeStart();


    int getRangeCount();


    void setProperty(String paramString, Object paramObject);


    Object getProperty(String paramString);


    Map getProperties();


    void setProperties(Map paramMap);


    void setLanguage(Language paramLanguage);


    Language getLanguage();


    SearchContext getCopy();
}

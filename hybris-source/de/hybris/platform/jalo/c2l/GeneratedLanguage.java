package de.hybris.platform.jalo.c2l;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedLanguage extends C2LItem
{
    public static final String FALLBACKLANGUAGES = "fallbackLanguages";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(C2LItem.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<Language> getFallbackLanguages()
    {
        return getFallbackLanguages(getSession().getSessionContext());
    }


    public void setFallbackLanguages(List<Language> value)
    {
        setFallbackLanguages(getSession().getSessionContext(), value);
    }


    public abstract List<Language> getFallbackLanguages(SessionContext paramSessionContext);


    public abstract void setFallbackLanguages(SessionContext paramSessionContext, List<Language> paramList);
}

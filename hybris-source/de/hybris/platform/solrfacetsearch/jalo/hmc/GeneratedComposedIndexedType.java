package de.hybris.platform.solrfacetsearch.jalo.hmc;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedComposedIndexedType extends GenericItem
{
    public static final String INDEXEDTYPE = "indexedType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    public void setIndexedType(String value)
    {
        setIndexedType(getSession().getSessionContext(), value);
    }


    public abstract String getIndexedType(SessionContext paramSessionContext);


    public abstract void setIndexedType(SessionContext paramSessionContext, String paramString);
}

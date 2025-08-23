package de.hybris.platform.jalo.test;

import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class GeneratedTestItem3 extends TestItem
{
    public static final String XXX = "xxx";
    public static final String PROP = "prop";
    public static final String PROP2 = "prop2";
    public static final String PROP3 = "prop3";
    public static final String ITEMTYPETWO = "itemTypeTwo";
    public static final String ITEMSTYPETWO = "itemsTypeTwo";


    public Collection<TestItem> getItemsTypeTwo(SessionContext ctx)
    {
        Collection<TestItem> coll = (Collection<TestItem>)getProperty(ctx, "itemsTypeTwo");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<TestItem> getItemsTypeTwo()
    {
        return getItemsTypeTwo(getSession().getSessionContext());
    }


    public void setItemsTypeTwo(SessionContext ctx, Collection<TestItem> value)
    {
        setProperty(ctx, "itemsTypeTwo", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setItemsTypeTwo(Collection<TestItem> value)
    {
        setItemsTypeTwo(getSession().getSessionContext(), value);
    }


    public TestItem getItemTypeTwo(SessionContext ctx)
    {
        return (TestItem)getProperty(ctx, "itemTypeTwo");
    }


    public TestItem getItemTypeTwo()
    {
        return getItemTypeTwo(getSession().getSessionContext());
    }


    public void setItemTypeTwo(SessionContext ctx, TestItem value)
    {
        setProperty(ctx, "itemTypeTwo", value);
    }


    public void setItemTypeTwo(TestItem value)
    {
        setItemTypeTwo(getSession().getSessionContext(), value);
    }


    public String getProp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "prop");
    }


    public String getProp()
    {
        return getProp(getSession().getSessionContext());
    }


    public void setProp(SessionContext ctx, String value)
    {
        setProperty(ctx, "prop", value);
    }


    public void setProp(String value)
    {
        setProp(getSession().getSessionContext(), value);
    }


    public String getProp2(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedTestItem3.getProp2 requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "prop2");
    }


    public String getProp2()
    {
        return getProp2(getSession().getSessionContext());
    }


    public Map<Language, String> getAllProp2(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "prop2", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllProp2()
    {
        return getAllProp2(getSession().getSessionContext());
    }


    public void setProp2(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedTestItem3.setProp2 requires a session language", 0);
        }
        setLocalizedProperty(ctx, "prop2", value);
    }


    public void setProp2(String value)
    {
        setProp2(getSession().getSessionContext(), value);
    }


    public void setAllProp2(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "prop2", value);
    }


    public void setAllProp2(Map<Language, String> value)
    {
        setAllProp2(getSession().getSessionContext(), value);
    }


    public String getProp3(SessionContext ctx)
    {
        return (String)getProperty(ctx, "prop3");
    }


    public String getProp3()
    {
        return getProp3(getSession().getSessionContext());
    }


    public void setProp3(SessionContext ctx, String value)
    {
        setProperty(ctx, "prop3", value);
    }


    public void setProp3(String value)
    {
        setProp3(getSession().getSessionContext(), value);
    }


    public String getXxx(SessionContext ctx)
    {
        return (String)getProperty(ctx, "xxx");
    }


    public String getXxx()
    {
        return getXxx(getSession().getSessionContext());
    }


    public void setXxx(SessionContext ctx, String value)
    {
        setProperty(ctx, "xxx", value);
    }


    public void setXxx(String value)
    {
        setXxx(getSession().getSessionContext(), value);
    }
}

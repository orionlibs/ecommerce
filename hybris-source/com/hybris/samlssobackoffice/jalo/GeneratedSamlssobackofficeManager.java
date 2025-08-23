package com.hybris.samlssobackoffice.jalo;

import com.hybris.samlssobackoffice.constants.GeneratedSamlssobackofficeConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.samlsinglesignon.jalo.SamlUserGroup;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSamlssobackofficeManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("enableBackofficeLogin", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.samlsinglesignon.jalo.SamlUserGroup", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Boolean isEnableBackofficeLogin(SessionContext ctx, GenericItem item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedSamlssobackofficeConstants.Attributes.SamlUserGroup.ENABLEBACKOFFICELOGIN);
    }


    public Boolean isEnableBackofficeLogin(SamlUserGroup item)
    {
        return isEnableBackofficeLogin(getSession().getSessionContext(), (GenericItem)item);
    }


    public boolean isEnableBackofficeLoginAsPrimitive(SessionContext ctx, SamlUserGroup item)
    {
        Boolean value = isEnableBackofficeLogin(ctx, (GenericItem)item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableBackofficeLoginAsPrimitive(SamlUserGroup item)
    {
        return isEnableBackofficeLoginAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setEnableBackofficeLogin(SessionContext ctx, GenericItem item, Boolean value)
    {
        item.setProperty(ctx, GeneratedSamlssobackofficeConstants.Attributes.SamlUserGroup.ENABLEBACKOFFICELOGIN, value);
    }


    public void setEnableBackofficeLogin(SamlUserGroup item, Boolean value)
    {
        setEnableBackofficeLogin(getSession().getSessionContext(), (GenericItem)item, value);
    }


    public void setEnableBackofficeLogin(SessionContext ctx, SamlUserGroup item, boolean value)
    {
        setEnableBackofficeLogin(ctx, (GenericItem)item, Boolean.valueOf(value));
    }


    public void setEnableBackofficeLogin(SamlUserGroup item, boolean value)
    {
        setEnableBackofficeLogin(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "samlssobackoffice";
    }
}

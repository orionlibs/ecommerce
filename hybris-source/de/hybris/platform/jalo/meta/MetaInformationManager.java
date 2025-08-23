package de.hybris.platform.jalo.meta;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.HybrisResourceBundle;
import de.hybris.platform.persistence.meta.MetaInformationManagerEJB;
import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class MetaInformationManager extends Manager
{
    public static final String BEAN_NAME = "core.metaInformationManager";
    public static final String SYSTEM_PK = "systemPK";
    public static final String SYSTEM_NAME = "systemName";
    public static final String LICENCE = "licence";
    public static final String HMC_RESOURCES = "de.hybris.platform.hmc.locales";


    public static MetaInformationManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getMetaInformationManager();
    }


    public Class getRemoteManagerClass()
    {
        return MetaInformationManagerEJB.class;
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        if(Boolean.TRUE.equals(ctx.getAttribute("is.hmc.session")))
        {
            JaloConnection.getInstance().logItemRemoval(item, false);
        }
    }


    public Set getPropertyNames()
    {
        return Collections.unmodifiableSet(((MetaInformationManagerEJB)getRemote()).getPropertyNames());
    }


    public Object setProperty(String name, Object value)
    {
        return wrap(((MetaInformationManagerEJB)getRemote()).setProperty(name,
                        WrapperFactory.unwrap(getTenant().getCache(), value, true)));
    }


    public Object getProperty(String name)
    {
        boolean[] isOK = new boolean[1];
        isOK[0] = true;
        Object ret = WrapperFactory.wrap(getSession().getSessionContext(), getTenant().getCache(), ((MetaInformationManagerEJB)
                        getRemote()).getPropertyRaw(name), (WrapperFactory.ItemPropertyWrappingListener)new Object(this, isOK));
        if(!isOK[0])
        {
            setProperty(name, ret);
        }
        return ret;
    }


    public Object removeProperty(String name)
    {
        return ((MetaInformationManagerEJB)getRemote()).removeProperty(name);
    }


    public void setResourceBundle(String basename, HybrisResourceBundle bundle)
    {
        setProperty(basename.concat(bundle.getLocale().toString()), bundle);
    }


    public HybrisResourceBundle getResourceBundle(String basename, Locale locale)
    {
        HybrisResourceBundle bundle = (HybrisResourceBundle)getProperty(basename.concat(locale.toString()));
        Locale parentLocale = getParentLocale(locale);
        if(bundle == null && parentLocale != null)
        {
            return getResourceBundle(basename, parentLocale);
        }
        if(bundle != null && parentLocale != null)
        {
            HybrisResourceBundle parent = getResourceBundle(basename, parentLocale);
            if(parent != null)
            {
                bundle.setParent((ResourceBundle)parent);
            }
        }
        return bundle;
    }


    private Locale getParentLocale(Locale locale)
    {
        if(locale.getVariant() != null && !locale.getVariant().equals(""))
        {
            return new Locale(locale.getLanguage(), locale.getCountry());
        }
        if(locale.getCountry() != null && !locale.getCountry().equals(""))
        {
            return new Locale(locale.getLanguage(), "");
        }
        if(locale.getLanguage() != null && !locale.getLanguage().equals(""))
        {
            return new Locale("", "");
        }
        return null;
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new MetaInformationManagerSerializableDTO(getTenant());
    }
}

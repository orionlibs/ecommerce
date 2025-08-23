package de.hybris.platform.persistence.c2l;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.persistence.ExtensibleItemEJBImpl;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.util.ItemPropertyValueCollection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class LocalizableItemEJBImpl extends ExtensibleItemEJBImpl implements LocalizableItem.LocalizableItemImpl
{
    protected LocalizableItemEJBImpl(Tenant tenant, LocalizableItemRemote remoteObject)
    {
        super(tenant, (ExtensibleItemRemote)remoteObject);
    }


    protected void checkLanguage(SessionContext ctx, String fieldName) throws JaloSystemException
    {
        if(ctx.getLanguage() == null)
        {
            throw new JaloSystemException(null, "cannot access localized field " + fieldName + " without setting language in session contex " + ctx, 0);
        }
    }


    protected PK getLangPK(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloSystemException(null, "cannot access localized field without setting language in session contex " + ctx, 0);
        }
        return ctx.getLanguage().getPK();
    }


    public Map<String, Object> getAllLocalizedProperties(SessionContext ctx)
    {
        Map<String, Object> ejbMap = ((LocalizableItemRemote)getRemote()).getAllLocalizedProperties(getLangPK(ctx));
        if(ejbMap.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Cache c = getCache();
        Map<String, Object> ret = new HashMap<>(ejbMap.size());
        for(Map.Entry<String, Object> e : ejbMap.entrySet())
        {
            MyItemPropertyWrappingListener l = new MyItemPropertyWrappingListener(this);
            Object jaloValue = WrapperFactory.wrap(ctx, c, e.getValue(), (WrapperFactory.ItemPropertyWrappingListener)l);
            if(l.error)
            {
                setLocalizedProperty(ctx, e.getKey(), jaloValue);
            }
            if(jaloValue != null)
            {
                ret.put(e.getKey(), jaloValue);
            }
        }
        return Collections.unmodifiableMap(ret);
    }


    public Set<String> getLocalizedPropertyNames(SessionContext ctx)
    {
        Set<String> coll = ((LocalizableItemRemote)getRemote()).getLocalizedPropertyNames(getLangPK(ctx));
        return Collections.unmodifiableSet(coll);
    }


    public Map<Language, ?> getAllLocalizedProperties(SessionContext ctx, String name, Set<Language> languages)
    {
        Cache c = getCache();
        MyItemPropertyWrappingListener l = new MyItemPropertyWrappingListener(this);
        Map<Language, ?> ret = (Map<Language, ?>)WrapperFactory.wrap(ctx, c, ((LocalizableItemRemote)
                        getRemote()).getAllLocalizedProperties(name,
                        (ItemPropertyValueCollection)WrapperFactory.unwrap(c, languages, true)), (WrapperFactory.ItemPropertyWrappingListener)l);
        if(l.error)
        {
            setAllLocalizableProperties(ctx, name, ret);
        }
        return ret;
    }


    public Map<Language, ?> setAllLocalizableProperties(SessionContext ctx, String name, Map<Language, ?> props)
    {
        Cache c = getCache();
        return (Map<Language, ?>)WrapperFactory.wrap(ctx, c, ((LocalizableItemRemote)
                        getRemote()).setAllLocalizedProperties(name,
                        (Map)WrapperFactory.unwrap(c, props, true)), null);
    }


    public Object getLocalizedProperty(SessionContext ctx, String name)
    {
        Cache c = getCache();
        MyItemPropertyWrappingListener l = new MyItemPropertyWrappingListener(this);
        Object s = WrapperFactory.wrap(ctx, c, ((LocalizableItemRemote)
                        getRemote()).getLocalizedProperty(name, getLangPK(ctx)), (WrapperFactory.ItemPropertyWrappingListener)l);
        if(l.error)
        {
            setLocalizedProperty(ctx, name, s);
        }
        return s;
    }


    public Object setLocalizedProperty(SessionContext ctx, String name, Object value)
    {
        Cache c = getCache();
        return WrapperFactory.wrap(c, ((LocalizableItemRemote)
                        getRemote()).setLocalizedProperty(name, getLangPK(ctx),
                        WrapperFactory.unwrap(c, value, true)));
    }


    public Object removeLocalizedProperty(SessionContext ctx, String name)
    {
        return wrap(((LocalizableItemRemote)getRemote()).removeLocalizedProperty(name, getLangPK(ctx)));
    }
}

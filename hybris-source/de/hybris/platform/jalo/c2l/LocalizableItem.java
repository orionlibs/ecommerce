package de.hybris.platform.jalo.c2l;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;

@ForceJALO(reason = "something else")
public abstract class LocalizableItem extends ExtensibleItem
{
    public static final String LANGUAGE_FALLBACK_ENABLED = "enable.language.fallback";
    private transient Map localizedPropertyValues;


    protected SessionContext getAllValuesSessionContext(SessionContext ctx)
    {
        SessionContext myCtx = (ctx != null) ? new SessionContext(ctx) : getSession().createSessionContext();
        myCtx.setLanguage(null);
        return myCtx;
    }


    private Map getBaseMap(boolean createNew)
    {
        if(this.localizedPropertyValues == null && createNew)
        {
            this.localizedPropertyValues = (Map)new CaseInsensitiveMap();
        }
        return (this.localizedPropertyValues != null) ? this.localizedPropertyValues : Collections.EMPTY_MAP;
    }


    private Map getLocalizedJaloOnlyPropertyMap(String name, boolean createNew)
    {
        CaseInsensitiveMap caseInsensitiveMap;
        Map map = (Map)getBaseMap(createNew).get(name);
        if(map == null && createNew)
        {
            getBaseMap(createNew).put(name, caseInsensitiveMap = new CaseInsensitiveMap());
        }
        return (caseInsensitiveMap != null) ? (Map)caseInsensitiveMap : Collections.EMPTY_MAP;
    }


    protected boolean hasLanguage(SessionContext ctx)
    {
        return (ctx != null && ctx.getLanguage() != null);
    }


    protected boolean isFallbackEnabled(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("enable.language.fallback")));
    }


    public Map getAllLocalizedProperties()
    {
        return getAllLocalizedProperties(getSession().getSessionContext());
    }


    public Map getAllLocalizedProperties(SessionContext ctx)
    {
        if(this.isJaloOnly)
        {
            if(!hasLanguage(ctx))
            {
                return Collections.unmodifiableMap(getBaseMap(false));
            }
            CaseInsensitiveMap<String, V> caseInsensitiveMap = new CaseInsensitiveMap();
            for(Iterator<Map.Entry> it = getBaseMap(false).entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry e = it.next();
                String name = (String)e.getKey();
                Map values = (Map)e.getValue();
                caseInsensitiveMap.put(name, (values != null) ? (V)values.get(ctx.getLanguage()) : null);
            }
            return (Map)caseInsensitiveMap;
        }
        return ((LocalizableItemImpl)this.impl).getAllLocalizedProperties(ctx);
    }


    public Set getLocalizedPropertyNames()
    {
        return getLocalizedPropertyNames(getSession().getSessionContext());
    }


    public Set getLocalizedPropertyNames(SessionContext ctx)
    {
        return this.isJaloOnly ? Collections.unmodifiableSet(getBaseMap(false).keySet()) : (
                        (LocalizableItemImpl)this.impl).getLocalizedPropertyNames(ctx);
    }


    public Object getLocalizedProperty(String name)
    {
        return getLocalizedProperty(getSession().getSessionContext(), name);
    }


    public Object getLocalizedProperty(SessionContext ctx, String name)
    {
        Object ret = getLocalizedPropertyInternal(ctx, name);
        if(isEmptyValue(ctx, name, ret) && isFallbackEnabled(ctx) && hasLanguage(ctx))
        {
            SessionContext fbCtx = new SessionContext(ctx);
            for(Iterator<Language> it = ctx.getLanguage().getFallbackLanguages().iterator(); ret == null && it.hasNext(); )
            {
                fbCtx.setLanguage(it.next());
                ret = getLocalizedPropertyInternal(fbCtx, name);
            }
        }
        return ret;
    }


    protected boolean isEmptyValue(SessionContext ctx, String name, Object value)
    {
        return (value == null);
    }


    protected Object getLocalizedPropertyInternal(SessionContext ctx, String name)
    {
        if(this.isJaloOnly)
        {
            if(hasLanguage(ctx))
            {
                return getLocalizedJaloOnlyPropertyMap(name, false).get(ctx.getLanguage());
            }
            return Collections.unmodifiableMap(getLocalizedJaloOnlyPropertyMap(name, false));
        }
        if(hasLanguage(ctx))
        {
            return (new Object(this, PlatformStringUtils.toLowerCaseCached(name), true, name))
                            .get(ctx);
        }
        return getAllLocalizedProperties(ctx, name, C2LManager.getInstance().getAllLanguages());
    }


    public Object setLocalizedProperty(String name, Object value)
    {
        return setLocalizedProperty(getSession().getSessionContext(), name, value);
    }


    public Object setLocalizedProperty(SessionContext ctx, String name, Object value)
    {
        if(value == null)
        {
            return removeLocalizedProperty(ctx, name);
        }
        if(this.isJaloOnly)
        {
            return hasLanguage(ctx) ? getLocalizedJaloOnlyPropertyMap(name, true).put(ctx.getLanguage(), value) : getBaseMap(
                            true).put(name, value);
        }
        if(hasLanguage(ctx))
        {
            return (new Object(this, PlatformStringUtils.toLowerCaseCached(name), true, name, value))
                            .set(ctx);
        }
        return setAllLocalizedProperties(ctx, name, (Map<Language, ?>)value);
    }


    public Map<Language, ?> setAllLocalizedProperties(SessionContext ctx, String name, Map<Language, ?> values)
    {
        return ((LocalizableItemImpl)getImplementation()).setAllLocalizableProperties(ctx, name, values);
    }


    public Map<Language, ?> setAllLocalizedProperties(String name, Map<Language, ?> values)
    {
        return setAllLocalizedProperties(getSession().getSessionContext(), name, values);
    }


    public Map<Language, ?> getAllLocalizedProperties(SessionContext ctx, String name, Set<Language> languages)
    {
        return (languages == null || languages.isEmpty()) ? Collections.EMPTY_MAP : (
                        (LocalizableItemImpl)getImplementation()).getAllLocalizedProperties(ctx, name, languages);
    }


    public Map<Language, ?> getAllLocalizedProperties(String name, Set<Language> languages)
    {
        return getAllLocalizedProperties(getSession().getSessionContext(), name, languages);
    }


    public Object removeLocalizedProperty(String name)
    {
        return removeLocalizedProperty(getSession().getSessionContext(), name);
    }


    public Object removeLocalizedProperty(SessionContext ctx, String name)
    {
        if(this.isJaloOnly)
        {
            return hasLanguage(ctx) ? getLocalizedJaloOnlyPropertyMap(name, false).remove(ctx.getLanguage()) : getBaseMap(
                            false).remove(name);
        }
        if(hasLanguage(ctx))
        {
            return (new Object(this, PlatformStringUtils.toLowerCaseCached(name), true, name))
                            .set(ctx);
        }
        Set<Language> languages = C2LManager.getInstance().getAllLanguages();
        Map<Language, Object> values = new HashMap<>(languages.size());
        for(Language l : languages)
        {
            values.put(l, null);
        }
        return setAllLocalizedProperties(ctx, name, values);
    }
}

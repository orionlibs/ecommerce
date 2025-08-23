package de.hybris.platform.util;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import java.util.Map;

public class JaloPropertyContainerAdapter implements JaloPropertyContainer
{
    private final JaloSession session;
    private EJBPropertyContainer cont;


    public JaloPropertyContainerAdapter(JaloSession jaloSession)
    {
        this.session = jaloSession;
    }


    public EJBPropertyContainer unwrap()
    {
        return this.cont;
    }


    private EJBPropertyContainer getEJBPropertyContainer()
    {
        if(this.cont == null)
        {
            this.cont = new EJBPropertyContainer();
        }
        return this.cont;
    }


    private Cache getCache()
    {
        return this.session.getTenant().getCache();
    }


    public void setProperty(String name, Object value)
    {
        getEJBPropertyContainer().setProperty(name, WrapperFactory.unwrap(getCache(), value, true));
    }


    public void setLocalizedProperty(String name, Object value)
    {
        setLocalizedProperty(this.session.getSessionContext(), name, WrapperFactory.unwrap(getCache(), value, true));
    }


    public void setLocalizedProperty(SessionContext ctx, String name, Object value)
    {
        getEJBPropertyContainer().setLocalizedProperty(name, ctx.getLanguage().getPK(),
                        WrapperFactory.unwrap(getCache(), value, true));
    }


    public void setAllLocalizedProperties(String name, Map<Language, Object> values)
    {
        EJBPropertyContainer cont = getEJBPropertyContainer();
        Map<ItemPropertyValue, Object> unwrappedValues = (Map<ItemPropertyValue, Object>)WrapperFactory.unwrap(getCache(), values, true);
        for(Map.Entry<ItemPropertyValue, Object> e : unwrappedValues.entrySet())
        {
            cont.setLocalizedProperty(name, ((ItemPropertyValue)e.getKey()).getPK(), e.getValue());
        }
    }


    public String toString()
    {
        return "JaloPropertyContainer(" + ((this.cont != null) ? this.cont.toPropertyString() : "") + ")";
    }


    public boolean isEmpty()
    {
        return (this.cont == null || this.cont.getPropertyMap().isEmpty());
    }


    public int size()
    {
        return (this.cont == null) ? 0 : this.cont.getPropertyMap().size();
    }
}

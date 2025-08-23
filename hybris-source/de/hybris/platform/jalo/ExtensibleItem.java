package de.hybris.platform.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.type.AttributeAccess;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.LocalizableFeatureAccess;
import de.hybris.platform.jalo.type.PropertyAccess;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;

@ForceJALO(reason = "something else")
public abstract class ExtensibleItem extends Item
{
    private Map jaloOnlyPropertyValues;


    public ExtensibleItemImpl getImplementation()
    {
        return (ExtensibleItemImpl)super.getImplementation();
    }


    protected AttributeAccess createNonClassAccessor(AttributeDescriptor fieldDescriptor)
    {
        AttributeAccess ret = super.createNonClassAccessor(fieldDescriptor);
        if(ret == null)
        {
            if(fieldDescriptor.isProperty())
            {
                if(accessorLog.isDebugEnabled())
                {
                    accessorLog.debug("no access for " + getClass().getName() + "." + fieldDescriptor.getQualifier() + " - creating property access");
                }
                ret = fieldDescriptor.isLocalized() ? (AttributeAccess)new LocalizableFeatureAccess(fieldDescriptor.getQualifier()) : (AttributeAccess)new PropertyAccess(fieldDescriptor.getQualifier());
            }
        }
        return ret;
    }


    private Map getJaloOnlyPropertyMap(boolean createNew)
    {
        if(this.jaloOnlyPropertyValues == null && createNew)
        {
            this.jaloOnlyPropertyValues = (Map)new CaseInsensitiveMap();
        }
        return (this.jaloOnlyPropertyValues != null) ? this.jaloOnlyPropertyValues : Collections.EMPTY_MAP;
    }


    public Map getAllProperties()
    {
        return getAllProperties(getSession().getSessionContext());
    }


    public Map getAllProperties(SessionContext ctx)
    {
        return this.isJaloOnly ? Collections.unmodifiableMap(getJaloOnlyPropertyMap(false)) :
                        getImplementation()
                                        .getAllProperties(ctx);
    }


    public Set getPropertyNames()
    {
        return getPropertyNames(getSession().getSessionContext());
    }


    public Set getPropertyNames(SessionContext ctx)
    {
        return this.isJaloOnly ? Collections.unmodifiableSet(getJaloOnlyPropertyMap(false).keySet()) :
                        getImplementation()
                                        .getPropertyNames(ctx);
    }


    public Object setProperty(String name, Object value)
    {
        return setProperty(getSession().getSessionContext(), name, value);
    }


    public Object setProperty(SessionContext ctx, String name, Object value)
    {
        if(this.isJaloOnly)
        {
            return (value != null) ? getJaloOnlyPropertyMap(true).put(name, value) : getJaloOnlyPropertyMap(true).remove(name);
        }
        return (new Object(this, PlatformStringUtils.toLowerCaseCached(name), name, value))
                        .set(ctx);
    }


    public Object getProperty(String name)
    {
        return getProperty(getSession().getSessionContext(), name);
    }


    public Object getProperty(SessionContext ctx, String name)
    {
        if(this.isJaloOnly)
        {
            return getJaloOnlyPropertyMap(false).get(name);
        }
        return (new Object(this, PlatformStringUtils.toLowerCaseCached(name), name))
                        .get(ctx);
    }


    public Object removeProperty(String name)
    {
        return removeProperty(getSession().getSessionContext(), name);
    }


    public Object removeProperty(SessionContext ctx, String name)
    {
        if(this.isJaloOnly)
        {
            return getJaloOnlyPropertyMap(false).remove(name);
        }
        return (new Object(this, PlatformStringUtils.toLowerCaseCached(name), name))
                        .set(ctx);
    }


    public void setAllProperties(JaloPropertyContainer propertyContainer) throws ConsistencyCheckException
    {
        setAllProperties(getSession().getSessionContext(), propertyContainer);
    }


    public void setAllProperties(SessionContext ctx, JaloPropertyContainer propertyContainer) throws ConsistencyCheckException
    {
        if(this.isJaloOnly)
        {
            throw new JaloSystemException("setAllProperties( SessionContext , JaloPropertyContainer) is not supported for JaloOnlyItems");
        }
        (new Object(this, propertyContainer))
                        .set(ctx);
    }
}

package de.hybris.platform.jalo.config;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloOnlySingletonItem;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.meta.MetaInformationManager;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ConfigAttributeDescriptor;
import java.io.Serializable;
import java.util.Date;

public class ConfigProxyItem extends Item implements JaloOnlySingletonItem
{
    private ComposedType composedType;
    private Date creationTime;
    private Date modificationTime;


    public PK providePK()
    {
        return PK.createFixedUUIDPK(2323, 1L);
    }


    public ComposedType provideComposedType()
    {
        return this.composedType;
    }


    public Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Class<ConfigProxyItem> clazz = type.getJaloClass();
        try
        {
            ConfigProxyItem newOne = clazz.newInstance();
            newOne.composedType = type;
            newOne.creationTime = new Date();
            newOne.modificationTime = newOne.creationTime;
            return newOne;
        }
        catch(ClassCastException e)
        {
            throw new JaloGenericCreationException("could not instantiate config proxy class " + clazz + " of type " + type
                            .getCode() + " : " + e, 0);
        }
        catch(InstantiationException e)
        {
            throw new JaloGenericCreationException("could not instantiate config proxy class " + clazz + " of type " + type
                            .getCode() + " : " + e, 0);
        }
        catch(IllegalAccessException e)
        {
            throw new JaloGenericCreationException("could not instantiate config proxy class " + clazz + " of type " + type
                            .getCode() + " : " + e, 0);
        }
    }


    public void removeJaloOnly() throws ConsistencyCheckException
    {
    }


    public Date provideCreationTime()
    {
        return this.creationTime;
    }


    public Date provideModificationTime()
    {
        return this.modificationTime;
    }


    public Object doGetAttribute(SessionContext ctx, String qualifier)
    {
        return getValue(ctx, qualifier);
    }


    public int getInt(String qualifier, int def)
    {
        String value = (String)getValue(qualifier);
        if(value == null)
        {
            return def;
        }
        return Integer.parseInt(value);
    }


    public Object getValue(String qualifier)
    {
        return getValue(JaloSession.getCurrentSession().getSessionContext(), qualifier);
    }


    public Object getValue(SessionContext ctx, String qualifier)
    {
        return storeInDatabase(qualifier) ? getPersistentValue(qualifier) : getTransientValue(qualifier);
    }


    public void doSetAttribute(SessionContext ctx, String qualifier, Object value) throws JaloBusinessException
    {
        setValue(ctx, qualifier, value);
    }


    public void setValue(String qualifier, Object value)
    {
        setValue(JaloSession.getCurrentSession().getSessionContext(), qualifier, value);
    }


    public void setValue(SessionContext ctx, String qualifier, Object value)
    {
        if(storeInDatabase(qualifier))
        {
            setPersistentValue(qualifier, (String)value);
        }
        else
        {
            setTransientValue(qualifier, (String)value);
        }
        this.modificationTime = new Date();
    }


    protected void setTransientValue(String qualifier, String value)
    {
        Config.setParameter(getExternalQualifier(qualifier), value);
    }


    protected String getTransientValue(String qualifier)
    {
        return Config.getParameter(getExternalQualifier(qualifier));
    }


    protected void setPersistentValue(String qualifier, String value)
    {
        String externalQualifier = getExternalQualifier(qualifier);
        MetaInformationManager.getInstance().setAttribute(externalQualifier, value);
    }


    protected Serializable getPersistentValue(String qualifier)
    {
        String externalQualifier = getExternalQualifier(qualifier);
        Serializable value = (Serializable)MetaInformationManager.getInstance().getAttribute(externalQualifier);
        if(value == null)
        {
            value = Config.getParameter(externalQualifier);
            if(value != null)
            {
                MetaInformationManager.getInstance().setAttribute(externalQualifier, value);
            }
        }
        return value;
    }


    protected boolean storeInDatabase(String qualifier)
    {
        AttributeDescriptor attributeDescriptor = getComposedType().getAttributeDescriptorIncludingPrivate(qualifier);
        return (attributeDescriptor instanceof ConfigAttributeDescriptor && ((ConfigAttributeDescriptor)attributeDescriptor).storeInDatabase());
    }


    protected String getExternalQualifier(String qualifier)
    {
        AttributeDescriptor attributeDescriptor = getComposedType().getAttributeDescriptorIncludingPrivate(qualifier);
        return (attributeDescriptor != null && attributeDescriptor instanceof ConfigAttributeDescriptor) ? (
                        (ConfigAttributeDescriptor)attributeDescriptor).getExternalQualifier() :
                        escapeKey(qualifier);
    }


    public static boolean booleanValue(String value)
    {
        if(value == null)
        {
            return false;
        }
        String valueUp = value.trim().toUpperCase(LocaleHelper.getPersistenceLocale());
        return ("TRUE".equals(valueUp) || "1".equals(valueUp) || "ON".equals(valueUp) || "JAWOLL".equals(valueUp) || "YES"
                        .equals(valueUp));
    }


    public static Integer integerValue(String value)
    {
        if(value == null)
        {
            return null;
        }
        try
        {
            return Integer.valueOf(value.trim());
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }


    public static Long longValue(String value)
    {
        if(value == null)
        {
            return null;
        }
        try
        {
            return Long.valueOf(value.trim());
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }


    private String escapeKey(String key)
    {
        int keyLength = key.length();
        StringBuilder escaped = new StringBuilder(keyLength);
        for(int i = 0; i < keyLength; i++)
        {
            char character = key.charAt(i);
            if(character != '_')
            {
                escaped.append(character);
            }
            else if(i < keyLength - 1 && key.charAt(i + 1) == '_')
            {
                escaped.append('_');
                i++;
            }
            else
            {
                escaped.append('.');
            }
        }
        return escaped.toString();
    }
}

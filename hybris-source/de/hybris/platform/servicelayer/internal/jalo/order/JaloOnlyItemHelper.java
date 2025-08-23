package de.hybris.platform.servicelayer.internal.jalo.order;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class JaloOnlyItemHelper implements JaloOnlyItem, Serializable
{
    private final PK pk;
    private final Item item;
    private ComposedType composedType;
    private Date creationTime;
    private Date modificationTime;


    public JaloOnlyItemHelper(PK pk, Item item, ComposedType type, Date creationTime, Date modifiedTime)
    {
        this.pk = pk;
        this.item = item;
        this.composedType = type;
        this.creationTime = creationTime;
        this.modificationTime = modifiedTime;
    }


    public final ComposedType provideComposedType()
    {
        return this.composedType;
    }


    public final Date provideCreationTime()
    {
        return this.creationTime;
    }


    public void setCreationTime(Date date)
    {
        this.creationTime = date;
    }


    public final Date provideModificationTime()
    {
        return this.modificationTime;
    }


    public final PK providePK()
    {
        return this.pk;
    }


    public void removeJaloOnly() throws ConsistencyCheckException
    {
        this.composedType = null;
        this.creationTime = null;
        this.modificationTime = null;
    }


    public Object doGetAttribute(SessionContext ctx, String attrQualifier) throws JaloInvalidParameterException, JaloSecurityException
    {
        throw new UnsupportedOperationException("JaloOnlyHelper.doGetAttribute");
    }


    public void doSetAttribute(SessionContext ctx, String attrQualifier, Object value) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        throw new UnsupportedOperationException("JaloOnlyHelper.doSetAttribute");
    }


    public void markModified()
    {
        this.modificationTime = new Date();
    }


    public void markModified(Date date)
    {
        this.modificationTime = date;
    }


    public <T> T getProperty(SessionContext ctx, String qualifier)
    {
        return getProperty(ctx, qualifier, null);
    }


    public <T> T getProperty(SessionContext ctx, String qualifier, T defaultValue)
    {
        try
        {
            T ret = (T)((ExtensibleItem)this.item).getProperty(ctx, qualifier);
            return (ret != null) ? ret : defaultValue;
        }
        catch(ClassCastException e)
        {
            if(!(this.item instanceof ExtensibleItem))
            {
                throw new IllegalStateException("jalo only item " + this.item + " is no extensible item - cannot get properties from it");
            }
            throw new JaloSystemException(e);
        }
    }


    public <T> void setProperty(SessionContext ctx, String qualifier, T newValue)
    {
        try
        {
            ((ExtensibleItem)this.item).setProperty(ctx, qualifier, newValue);
            markModified();
        }
        catch(ClassCastException e)
        {
            if(!(this.item instanceof ExtensibleItem))
            {
                throw new IllegalStateException("jalo only item " + this.item + " is no extensible item - cannot get properties from it");
            }
            throw new JaloSystemException(e);
        }
    }


    public <T extends Collection> T getPropertyCollection(SessionContext ctx, String qualifier, T defaultValue)
    {
        return (T)getProperty(ctx, qualifier, (Collection)defaultValue);
    }


    public void setPropertyCollection(SessionContext ctx, String qualifier, Collection newValue, Class<? extends Collection> collectionClass)
    {
        Collection current = getProperty(ctx, qualifier, null);
        if(current != null)
        {
            current.clear();
            if(newValue != null)
            {
                current.addAll(newValue);
            }
        }
        else if(newValue != null)
        {
            try
            {
                current = collectionClass.newInstance();
                current.addAll(newValue);
                setProperty(ctx, qualifier, current);
            }
            catch(Exception e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    public void addToCollectionProperty(SessionContext ctx, String qualifier, Object element, Class<? extends Collection> collectionClass)
    {
        addToCollectionProperty(ctx, qualifier, Collections.singleton(element), collectionClass);
    }


    public void addToCollectionProperty(SessionContext ctx, String qualifier, Collection elements, Class<? extends Collection> collectionClass)
    {
        if(elements != null && !elements.isEmpty())
        {
            Collection current = getProperty(ctx, qualifier, null);
            if(current == null)
            {
                try
                {
                    current = collectionClass.newInstance();
                    current.addAll(elements);
                    setProperty(ctx, qualifier, current);
                }
                catch(Exception e)
                {
                    throw new JaloSystemException(e);
                }
            }
            else
            {
                current.addAll(elements);
            }
        }
    }


    public void removeFromCollectionProperty(SessionContext ctx, String qualifier, Object element)
    {
        Collection current = getProperty(ctx, qualifier, null);
        if(current != null)
        {
            current.remove(element);
        }
    }


    public void removeFromCollectionProperty(SessionContext ctx, String qualifier, Collection<?> elements)
    {
        Collection current = getProperty(ctx, qualifier, null);
        if(current != null)
        {
            current.removeAll(elements);
        }
    }


    public void removeCollectionProperty(SessionContext ctx, String qualifier)
    {
        setProperty(ctx, qualifier, null);
    }


    public int getPropertyInt(SessionContext ctx, String qualifier, int defaultValue)
    {
        return ((Integer)getProperty(ctx, qualifier, Integer.valueOf(defaultValue))).intValue();
    }


    public long getPropertyLong(SessionContext ctx, String qualifier, long defaultValue)
    {
        return ((Long)getProperty(ctx, qualifier, Long.valueOf(defaultValue))).longValue();
    }


    public double getPropertyDouble(SessionContext ctx, String qualifier, double defaultValue)
    {
        return ((Double)getProperty(ctx, qualifier, Double.valueOf(defaultValue))).doubleValue();
    }


    public boolean getPropertyBoolean(SessionContext ctx, String qualifier, boolean defaultValue)
    {
        return ((Boolean)getProperty(ctx, qualifier, Boolean.valueOf(defaultValue))).booleanValue();
    }
}

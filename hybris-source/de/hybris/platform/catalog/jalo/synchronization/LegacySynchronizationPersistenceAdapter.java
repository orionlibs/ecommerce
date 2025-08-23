package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.RecoverableSynchronizationPersistenceException;
import de.hybris.platform.catalog.SynchronizationPersistenceAdapter;
import de.hybris.platform.catalog.SynchronizationPersistenceException;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.dao.RecoverableDataAccessException;

public class LegacySynchronizationPersistenceAdapter implements SynchronizationPersistenceAdapter<Item, ComposedType>
{
    private static final Logger LOG = Logger.getLogger(LegacySynchronizationPersistenceAdapter.class);
    private final GenericCatalogCopyContext copyContext;


    public LegacySynchronizationPersistenceAdapter(GenericCatalogCopyContext copyContext)
    {
        this.copyContext = copyContext;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Created  a <<" + getClass().getSimpleName() + ">> implementation of the <<SynchronizationPersistenceAdapter>>");
        }
    }


    public Item create(ComposedType expectedType, Map<String, Object> attributes) throws SynchronizationPersistenceException
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("creating ... " + expectedType.getCode() + " with attributes  " + attributes.keySet());
            }
            return expectedType.newInstance(this.copyContext.getCtx(), attributes);
        }
        catch(Exception e)
        {
            throw new SynchronizationPersistenceException(e);
        }
    }


    public Map<String, Object> read(Item item, Set<String> attributes) throws SynchronizationPersistenceException
    {
        try
        {
            return item.getAllAttributes(getCopyContext().getCtx(), attributes);
        }
        catch(JaloSecurityException e)
        {
            throw new SynchronizationPersistenceException(e);
        }
    }


    public Map<String, Object> readLocalized(Item item, Set<String> attributes, Set<Language> languages) throws SynchronizationPersistenceException
    {
        Item.ItemAttributeMap locSourceValues = new Item.ItemAttributeMap();
        SessionContext lCtx = new SessionContext(getCopyContext().getCtx());
        for(String attr : attributes)
        {
            Map<Language, Object> values = new LinkedHashMap<>(languages.size());
            for(Language lang : languages)
            {
                lCtx.setLanguage(lang);
                try
                {
                    values.put(lang, item.getAttribute(lCtx, attr));
                }
                catch(JaloSecurityException e)
                {
                    e.printStackTrace();
                }
            }
            locSourceValues.put(attr, values);
        }
        return (Map<String, Object>)locSourceValues;
    }


    public void remove(Item entity) throws SynchronizationPersistenceException, RecoverableDataAccessException
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("removing " + entity);
            }
            entity.remove(this.copyContext.getCtx());
        }
        catch(Exception e)
        {
            handleRemovalException(entity, e);
        }
    }


    protected void handleRemovalException(Item itemToRemove, Throwable exception) throws SynchronizationPersistenceException, RecoverableDataAccessException
    {
        boolean canIgnore = canIgnoreItemRemovedException(itemToRemove, exception);
        if(!canIgnore)
        {
            if(shouldRetry(exception))
            {
                throw new RecoverableSynchronizationPersistenceException(exception);
            }
            throw new SynchronizationPersistenceException(exception);
        }
    }


    protected boolean shouldRetry(Throwable exception)
    {
        return exception instanceof de.hybris.platform.jalo.ConsistencyCheckException;
    }


    protected boolean canIgnoreItemRemovedException(Item itemToRemove, Throwable exception)
    {
        if(exception instanceof de.hybris.platform.jalo.JaloItemNotFoundException)
        {
            if(exception.getMessage().contains(itemToRemove.getPK().toString()))
            {
                return true;
            }
            LOG.warn("got JaloItemNotFoundException [" + exception + "] which doesnt seem to belong to scheduled item " + itemToRemove
                            .getPK() + "(" + itemToRemove.getComposedType().getCode() + ")");
        }
        else if(exception instanceof JaloObjectNoLongerValidException)
        {
            PK failedPK = ((JaloObjectNoLongerValidException)exception).getJaloObjectPK();
            if(itemToRemove.getPK().equals(failedPK))
            {
                return true;
            }
            LOG.warn("got JaloObjectNoLongerValidException [" + exception + "] which doesnt seem to belong to scheduled item " + itemToRemove
                            .getPK() + "(" + itemToRemove.getComposedType().getCode() + ")");
        }
        return false;
    }


    public void update(Item entity, Map.Entry<String, Object> attribute) throws SynchronizationPersistenceException
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("updating ... " + entity + " with attribute (" + (String)attribute.getKey() + "," + attribute.getValue() + ")");
            }
            entity.setAttribute(this.copyContext.getCtx(), attribute.getKey(), attribute.getValue());
        }
        catch(Exception e)
        {
            throw new SynchronizationPersistenceException(e);
        }
    }


    public void update(Item entity, Map<String, Object> attributes) throws SynchronizationPersistenceException
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("updating ... " + entity + " with attributes " + attributes.keySet());
            }
            entity.setAllAttributes(this.copyContext.getCtx(), attributes);
        }
        catch(Exception e)
        {
            throw new SynchronizationPersistenceException(e);
        }
    }


    public void resetUnitOfWork()
    {
    }


    public void disableTransactions()
    {
    }


    public void clearTransactionsSettings()
    {
    }


    protected GenericCatalogCopyContext getCopyContext()
    {
        return this.copyContext;
    }
}

package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.internal.converter.ReadParams;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JaloPersistenceObject implements PersistenceObjectInternal
{
    private volatile Item item;
    private volatile boolean refreshRequested = false;
    private final String typeCode;


    public JaloPersistenceObject(Item item)
    {
        this.item = Objects.<Item>requireNonNull(item);
        this.typeCode = item.getComposedType().getCode();
    }


    public boolean isBackedByJaloItem()
    {
        return true;
    }


    public Item getCorrespondingItem()
    {
        return this.item;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public PK getPK()
    {
        return this.item.getPK();
    }


    public long getPersistenceVersion()
    {
        return this.item.getPersistenceVersion();
    }


    public boolean isAlive()
    {
        return this.item.isAlive();
    }


    public PersistenceObjectInternal getLatest()
    {
        Item cacheBoundItem = this.refreshRequested ? this.item.getAndCheckCacheBoundItem() : this.item.getCacheBoundItem();
        this.refreshRequested = false;
        this.item = cacheBoundItem;
        return this;
    }


    public void refresh()
    {
        this.refreshRequested = true;
    }


    public Object readRawValue(ReadParams readParams)
    {
        Objects.requireNonNull(readParams, "readParams mustn't be null");
        SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
        try
        {
            if(readParams.isLocalized())
            {
                readParams.getI18nService().setCurrentLocale(readParams.getLocale());
            }
            if(Boolean.TRUE.equals(ctx.getAttribute("enable.language.fallback")))
            {
                ctx.removeAttribute("enable.language.fallback");
            }
            return this.item.getAttribute(ctx, readParams.getSingleQualifier());
        }
        catch(JaloInvalidParameterException | de.hybris.platform.jalo.security.JaloSecurityException e)
        {
            throw new ModelLoadingException("Error while reading attribute " + readParams.getSingleQualifier(), e);
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
    }


    public Map<String, Object> readRawValues(ReadParams readParams)
    {
        Objects.requireNonNull(readParams, "readParams mustn't be null");
        HashMap<String, Object> ret = new HashMap<>();
        SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
        try
        {
            if(readParams.isLocalized())
            {
                readParams.getI18nService().setCurrentLocale(readParams.getLocale());
            }
            if(Boolean.TRUE.equals(ctx.getAttribute("enable.language.fallback")))
            {
                ctx.removeAttribute("enable.language.fallback");
            }
            Map<String, Object> result = this.item.getAllAttributes(ctx, readParams.getAllQualifiers());
            for(Map.Entry<String, Object> e : result.entrySet())
            {
                ret.put(e.getKey(), e.getValue());
            }
        }
        catch(Exception e)
        {
            for(String q : readParams.getAllQualifiers())
            {
                try
                {
                    ret.put(q, this.item.getAttribute(ctx, q));
                }
                catch(Exception ex)
                {
                    ret.put(q, ex);
                }
            }
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return ret;
    }


    public boolean isEnumerationType()
    {
        return this.item instanceof de.hybris.platform.jalo.enumeration.EnumerationType;
    }
}

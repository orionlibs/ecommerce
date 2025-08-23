package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.converter.ReadParams;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

class SLDPersistenceObjectWithFallback implements PersistenceObjectInternal
{
    static final Logger LOG = Logger.getLogger(SLDPersistenceObjectWithFallback.class);
    private PersistenceObjectInternal delegate;
    private final SourceTransformer sourceTransformer;


    public SLDPersistenceObjectWithFallback(PersistenceObjectInternal delegate, SourceTransformer sourceTransformer)
    {
        Objects.requireNonNull(delegate, "delegate mustn't be null");
        Objects.requireNonNull(sourceTransformer, "sourceTransformer mustn't be null");
        this.delegate = delegate;
        this.sourceTransformer = sourceTransformer;
    }


    public boolean isBackedByJaloItem()
    {
        return this.delegate.isBackedByJaloItem();
    }


    public Item getCorrespondingItem()
    {
        return this.delegate.getCorrespondingItem();
    }


    public String getTypeCode()
    {
        return this.delegate.getTypeCode();
    }


    public PK getPK()
    {
        return this.delegate.getPK();
    }


    public long getPersistenceVersion()
    {
        return this.delegate.getPersistenceVersion();
    }


    public boolean isAlive()
    {
        return this.delegate.isAlive();
    }


    public PersistenceObjectInternal getLatest()
    {
        PersistenceObjectInternal latest = this.delegate.getLatest();
        if(latest.isBackedByJaloItem())
        {
            return latest;
        }
        this.delegate = latest;
        return this;
    }


    public Object readRawValue(ReadParams readParams)
    {
        fallbackToJaloIfNeeded(readParams);
        try
        {
            return this.delegate.readRawValue(readParams);
        }
        catch(CantReadValueFromPersistenceObject e)
        {
            handleCantReadValueFromPersistenceObject(e);
            return readRawValue(readParams);
        }
    }


    public Map<String, Object> readRawValues(ReadParams readParams)
    {
        fallbackToJaloIfNeeded(readParams);
        try
        {
            return this.delegate.readRawValues(readParams);
        }
        catch(CantReadValueFromPersistenceObject e)
        {
            handleCantReadValueFromPersistenceObject(e);
            return readRawValues(readParams);
        }
    }


    public boolean isEnumerationType()
    {
        return this.delegate.isEnumerationType();
    }


    private void handleCantReadValueFromPersistenceObject(CantReadValueFromPersistenceObject exception)
    {
        LOG.error("Error occured during reading.", (Throwable)exception);
        if(isBackedByJaloItem())
        {
            throw exception;
        }
        LOG.warn("Falling back to JALO for " + this.delegate.getTypeCode() + "[" + this.delegate.getPK() + "] because of not beeing able to read value.", (Throwable)exception);
        fallbackToJalo();
    }


    private void fallbackToJaloIfNeeded(ReadParams readParams)
    {
        if(!isBackedByJaloItem() && readParams.isAttemptToReadUnsupportedAttributes())
        {
            LOG.warn("Falling back to JALO for " + this.delegate.getTypeCode() + "[" + this.delegate.getPK() + "] because of reading the following attributes " + readParams
                            .getAllQualifiers());
            fallbackToJalo();
        }
    }


    private void fallbackToJalo()
    {
        this.delegate = this.sourceTransformer.getJALOPersistenceObject(getPK());
    }
}

package de.hybris.platform.servicelayer.keygenerator.impl;

import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeries;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;

public class PersistentKeyGenerator implements KeyGenerator
{
    private static final Logger LOG = Logger.getLogger(PersistentKeyGenerator.class);
    protected String key;
    protected int digits;
    protected String start;
    protected String template;
    protected boolean numeric;
    private boolean deprecatedTypeConfig = false;
    private boolean numberSeriesApiUsed = false;
    private Type type = Type.alphanumeric;


    @PostConstruct
    public void initializeKeyGenerator()
    {
        if(this.deprecatedTypeConfig)
        {
            this.type = convertDeprecatedType();
            LOG.warn(this.key + " generator is using deprecated 'numeric' property! Initializing as " + this.key);
        }
        if(this.numberSeriesApiUsed && this.type.equals(Type.uuid))
        {
            LOG.warn(this.key + " generator is of " + this.key + " type - omitting unsupported NumberSeries configuration settings");
        }
    }


    private Type convertDeprecatedType()
    {
        if(this.numeric)
        {
            return Type.numeric;
        }
        return Type.alphanumeric;
    }


    public Object generate()
    {
        if(this.type == Type.uuid)
        {
            return UUID.randomUUID().toString();
        }
        NumberSeriesManager nsm = NumberSeriesManager.getInstance();
        try
        {
            return nsm.getUniqueNumber(this.key, this.digits);
        }
        catch(JaloInvalidParameterException e)
        {
            if(JaloConnection.getInstance().isSystemInitialized())
            {
                if(createSeriesAfterLookupError(nsm) == null)
                {
                    throw e;
                }
                return nsm.getUniqueNumber(this.key, this.digits);
            }
            throw new RuntimeException("System is not initialized");
        }
    }


    public Object generateFor(Object object)
    {
        throw new UnsupportedOperationException("Not supported, please use the generate() method");
    }


    public void reset()
    {
        if(this.type.resettable)
        {
            NumberSeriesManager nsm = NumberSeriesManager.getInstance();
            nsm.resetNumberSeries(this.key, this.start, this.type.number, this.template);
        }
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Init call for PersistentKeyGenerator(" + this.key + ") found. It is deprecated and has no effect, please remove it");
        }
    }


    protected NumberSeries lookup(NumberSeriesManager nsm) throws JaloInvalidParameterException
    {
        NumberSeries series = null;
        try
        {
            series = nsm.getNumberSeries(this.key);
        }
        catch(IllegalStateException e)
        {
            if(Thread.currentThread().isInterrupted())
            {
                return null;
            }
            LOG.error(e);
        }
        return series;
    }


    protected NumberSeries createSeriesAfterLookupError(NumberSeriesManager nsm)
    {
        try
        {
            return nsm.createNumberSeries(this.key, this.start, this.type.number, this.digits, this.template);
        }
        catch(JaloInvalidParameterException e)
        {
            try
            {
                return lookup(nsm);
            }
            catch(JaloInvalidParameterException ex)
            {
                LOG.error("could not create series '" + this.key + "' after lookup failure: error1=" + e
                                .getMessage() + " error2=" + ex
                                .getMessage(), (Throwable)ex);
                return null;
            }
        }
    }


    public void setTemplate(String template)
    {
        this.template = template;
        this.numberSeriesApiUsed = true;
    }


    public void setKey(String key)
    {
        this.key = key;
        this.numberSeriesApiUsed = true;
    }


    public void setDigits(int digits)
    {
        this.digits = digits;
        this.numberSeriesApiUsed = true;
    }


    public void setStart(String start)
    {
        this.start = start;
        this.numberSeriesApiUsed = true;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setNumeric(boolean numeric)
    {
        this.numeric = numeric;
        this.deprecatedTypeConfig = true;
    }


    public void setType(Type type)
    {
        this.type = type;
    }
}

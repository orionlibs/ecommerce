package de.hybris.platform.jalo.numberseries;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.JspContext;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import org.apache.log4j.Logger;

public class NumberSeriesManager extends Manager
{
    private static final Logger log = Logger.getLogger(NumberSeriesManager.class);
    public static final String BEAN_NAME = "core.numberSeriesManager";
    private static final String DIGITS = "-DIGITS";
    private static final String STARTVALUE = "-STARTVALUE";
    public static final String CONFIG_PARAM_NUMBER_CACHE = "numberseries.cache.size";
    public static final int DEFAULT_NUMBER_CACHE_SIZE = 1000;


    public static NumberSeriesManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getNumberSeriesManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    public String getUniqueNumber(String key, int digits) throws JaloInvalidParameterException
    {
        return getUniqueNumberUnformatted(key).getFormatted(checkDigits(key, digits));
    }


    protected int checkDigits(String key, int digits)
    {
        return (digits > 0) ? digits : getDigits(key);
    }


    public String getUniqueNumber(String key) throws JaloInvalidParameterException
    {
        return getUniqueNumber(key, -1);
    }


    public NumberSeries getUniqueNumberUnformatted(String key) throws JaloInvalidParameterException
    {
        try
        {
            return getTenant().getSerialNumberGenerator().getUniqueNumber(key);
        }
        catch(IllegalArgumentException e)
        {
            if("customer_id".equalsIgnoreCase(key))
            {
                getOrCreateCustomerIDSeries();
                try
                {
                    return getTenant().getSerialNumberGenerator().getUniqueNumber(key);
                }
                catch(IllegalArgumentException e1)
                {
                    throw new JaloInvalidParameterException(e1.getMessage(), 0);
                }
            }
            throw new JaloInvalidParameterException(e.getMessage(), 0);
        }
    }


    public void removeNumberSeries(String key) throws JaloInvalidParameterException
    {
        try
        {
            getTenant().getSerialNumberGenerator().removeSeries(key);
            removeStartValue(key);
            removeDigits(key);
        }
        catch(IllegalArgumentException e)
        {
            if(!"order_code".equalsIgnoreCase(key) &&
                            !"customer_id".equalsIgnoreCase(key))
            {
                throw new JaloInvalidParameterException(e.getMessage(), 0);
            }
        }
    }


    @Deprecated(since = "5.0", forRemoval = false)
    public NumberSeries createNumberSeries(String key, String startValue, int type) throws JaloInvalidParameterException
    {
        return createNumberSeries(key, startValue, type, -1, null);
    }


    public NumberSeries createNumberSeries(String key, String startValue, int type, int digits, String template) throws JaloInvalidParameterException
    {
        try
        {
            NumberSeries ret = new NumberSeries(key, startValue, type, template);
            getTenant().getSerialNumberGenerator().createSeries(key, type, ret.getCurrentNumber(), ret.getTemplate());
            int cleanDigits = (digits > 0) ? digits : ((startValue != null) ? startValue.length() : -1);
            setStartValue(key, ret.getFormatted(cleanDigits));
            setDigits(key, cleanDigits);
            return ret;
        }
        catch(IllegalArgumentException e)
        {
            throw new JaloInvalidParameterException(e.getMessage(), 0);
        }
    }


    @Deprecated(since = "5.0", forRemoval = false)
    public NumberSeries createNumberSeries(String key, String startValue, int type, int digits) throws JaloInvalidParameterException
    {
        return createNumberSeries(key, startValue, type, digits, null);
    }


    public Collection<String> getAllNumberSeriesKeys()
    {
        Collection<String> ret = new LinkedHashSet<>();
        for(NumberSeries ns : getTenant().getSerialNumberGenerator().getAllInfo())
        {
            ret.add(ns.getKey());
        }
        return ret;
    }


    public Collection getAllNumberSeries()
    {
        return getTenant().getSerialNumberGenerator().getAllInfo();
    }


    public NumberSeries getNumberSeries(String key) throws JaloInvalidParameterException
    {
        try
        {
            return getTenant().getSerialNumberGenerator().getInfo(key);
        }
        catch(IllegalArgumentException e)
        {
            throw new JaloInvalidParameterException(e.getMessage(), 0);
        }
    }


    @Deprecated(since = "5.0", forRemoval = false)
    public void resetNumberSeries(String key, String startValue, int type) throws JaloInvalidParameterException
    {
        resetNumberSeries(key, startValue, type, null);
    }


    public void resetNumberSeries(String key, String startValue, int type, String template) throws JaloInvalidParameterException
    {
        NumberSeries tmp = new NumberSeries(key, startValue, type, template);
        try
        {
            getTenant().getSerialNumberGenerator().resetSeries(key, type, tmp.getCurrentNumber());
        }
        catch(IllegalArgumentException e)
        {
            if("customer_id".equalsIgnoreCase(key))
            {
                getOrCreateCustomerIDSeries();
                try
                {
                    getTenant().getSerialNumberGenerator().resetSeries(key, type, tmp.getCurrentNumber());
                }
                catch(IllegalArgumentException e1)
                {
                    throw new JaloInvalidParameterException(e1.getMessage(), 0);
                }
            }
            else
            {
                throw new JaloInvalidParameterException(e.getMessage(), 0);
            }
        }
    }


    public void setDigits(String key, int digits)
    {
        getTenant().getJaloConnection()
                        .getMetaInformationManager()
                        .setProperty(getDigitsPropertyKey(key), Integer.valueOf(digits));
    }


    public int getDigits(String key)
    {
        Integer digits = (Integer)getTenant().getJaloConnection().getMetaInformationManager().getProperty(
                        getDigitsPropertyKey(key));
        if(digits != null)
        {
            return digits.intValue();
        }
        return -1;
    }


    public void removeDigits(String key)
    {
        getTenant().getJaloConnection().getMetaInformationManager().removeProperty(getDigitsPropertyKey(key));
    }


    public void setStartValue(String key, String startvalue)
    {
        getTenant().getJaloConnection().getMetaInformationManager().setProperty(getStartValuePropertyKey(key), startvalue);
    }


    public String getStartValue(String key)
    {
        return (String)getTenant().getJaloConnection().getMetaInformationManager().getProperty(getStartValuePropertyKey(key));
    }


    public void removeStartValue(String key)
    {
        getTenant().getJaloConnection().getMetaInformationManager().removeProperty(getStartValuePropertyKey(key));
    }


    public boolean isCreatorDisabled()
    {
        return true;
    }


    public void createEssentialData(Map params, JspContext jspc)
    {
        getOrCreateCustomerIDSeries();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void assureCustomerIDSeriesCreated()
    {
        getOrCreateCustomerIDSeries();
    }


    public NumberSeries getOrCreateCustomerIDSeries()
    {
        NumberSeries series = null;
        try
        {
            series = getNumberSeries("customer_id");
        }
        catch(JaloInvalidParameterException e)
        {
            if(log.isDebugEnabled())
            {
                log.debug(e);
            }
        }
        return series;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void assureOrderCodeSeriesCreated()
    {
        getOrCreateOrderCodeSeries();
    }


    public NumberSeries getOrCreateOrderCodeSeries()
    {
        NumberSeries series = null;
        try
        {
            series = getNumberSeries("order_code");
        }
        catch(JaloInvalidParameterException e)
        {
            if(log.isDebugEnabled())
            {
                log.debug(e);
            }
        }
        return series;
    }


    private static final String getDigitsPropertyKey(String key)
    {
        return key + "-DIGITS";
    }


    private static final String getStartValuePropertyKey(String key)
    {
        return key + "-STARTVALUE";
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new NumberSeriesManagerSerializableDTO(getTenant());
    }
}

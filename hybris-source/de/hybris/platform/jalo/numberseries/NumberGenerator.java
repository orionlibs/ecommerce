package de.hybris.platform.jalo.numberseries;

import de.hybris.platform.persistence.EJBInvalidParameterException;
import java.util.Collection;
import javax.sql.DataSource;

public class NumberGenerator
{
    public static final String KEY = "serieskey";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CURRENT_NUMBER = "currentNumber";
    public static final String CURRENT_LONG = "currentValue";
    public static final String TYPE = "seriestype";
    public static final String CONFIG_PARAM_SYNCHRONIZE_NUMBERGENERATION = "numberseries.synchronize.generation";
    public static final String CONFIG_PARAM_MAX_RETRIES = "numberseries.maxretries";
    public static final String TEMPLATE = "template";


    @Deprecated(since = "ages", forRemoval = false)
    public static final void createNumberSequence(DataSource dataSource, String key, String startValue, int type) throws EJBInvalidParameterException
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final Collection getAllNumberSequenceKeys(DataSource dataSource)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final Collection getAllNumberSequences(DataSource dataSource)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final NumberSeries getNumberSequence(DataSource dataSource, String key)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final void resetNumberSequence(DataSource dataSource, String key, String startValue, int type) throws EJBInvalidParameterException
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final void removeNumberSequence(DataSource dataSource, String key) throws EJBInvalidParameterException
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final void removeAllNumberSequences(DataSource dataSource)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final String getUniqueNumber(DataSource dataSource, String key, int digits) throws EJBInvalidParameterException
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final String[] getUniqueNumbers(DataSource dataSource, String key, int count) throws EJBInvalidParameterException
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static final String format(String number, int digits)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static void initialize(DataSource dataSource, String file) throws EJBInvalidParameterException
    {
        throw new UnsupportedOperationException();
    }
}

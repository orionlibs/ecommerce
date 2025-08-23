package de.hybris.platform.persistence.numberseries;

import de.hybris.platform.jalo.numberseries.NumberSeries;
import java.util.Collection;

public interface SerialNumberGenerator
{
    Collection<NumberSeries> getAllInfo();


    NumberSeries getUniqueNumber(String paramString);


    NumberSeries getInfo(String paramString);


    @Deprecated(since = "5.0", forRemoval = true)
    void createSeries(String paramString, int paramInt, long paramLong);


    void createSeries(String paramString1, int paramInt, long paramLong, String paramString2);


    void removeSeries(String paramString);


    void resetSeries(String paramString, int paramInt, long paramLong);


    void clearAll();
}

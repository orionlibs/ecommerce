package de.hybris.platform.persistence.numberseries;

import de.hybris.platform.jalo.numberseries.NumberSeries;
import java.util.Collection;

public interface SerialNumberDAO
{
    NumberSeries fetchUniqueRange(String paramString, int paramInt);


    NumberSeries getCurrent(String paramString);


    Collection<NumberSeries> getAllCurrent();


    @Deprecated(since = "ages", forRemoval = true)
    void createSeries(String paramString, int paramInt, long paramLong);


    void createSeries(String paramString1, int paramInt, long paramLong, String paramString2);


    void removeSeries(String paramString);


    void resetSeries(String paramString, int paramInt, long paramLong);
}

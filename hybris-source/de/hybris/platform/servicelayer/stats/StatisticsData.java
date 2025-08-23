package de.hybris.platform.servicelayer.stats;

public interface StatisticsData
{
    boolean addDataCollector(String paramString);


    boolean removeDataCollector(String paramString);


    boolean containsDataCollector(String paramString);


    boolean putData(String paramString, long paramLong, float paramFloat);


    Object[][] getTimePeriodData(String paramString, long paramLong1, long paramLong2, long paramLong3);


    Object[][] getAllData(String paramString, int paramInt, long paramLong);


    Object[][] getTickAsArray(String paramString, long paramLong1, int paramInt, long paramLong2);


    int getCurrentSize(String paramString);


    void balanceDataLevels(String paramString);


    float getCollectorValue(String paramString, long paramLong);
}

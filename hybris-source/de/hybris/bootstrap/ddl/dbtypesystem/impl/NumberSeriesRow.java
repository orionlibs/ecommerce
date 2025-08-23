package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Objects;

public class NumberSeriesRow implements Row
{
    private Long hjmpTS;
    private String seriesKey;
    private Integer seriesType;
    private Long currentValue;


    public Long getHjmpTS()
    {
        return this.hjmpTS;
    }


    public void setHjmpTS(Long hjmpTS)
    {
        this.hjmpTS = hjmpTS;
    }


    public String getSeriesKey()
    {
        return this.seriesKey;
    }


    public void setSeriesKey(String seriesKey)
    {
        this.seriesKey = seriesKey;
    }


    public Integer getSeriesType()
    {
        return this.seriesType;
    }


    public void setSeriesType(Integer seriesType)
    {
        this.seriesType = seriesType;
    }


    public Long getCurrentValue()
    {
        return this.currentValue;
    }


    public void setCurrentValue(Long currentValue)
    {
        this.currentValue = currentValue;
    }


    public Object getValue(String columnName)
    {
        Objects.requireNonNull(columnName);
        switch(columnName.toLowerCase(LocaleHelper.getPersistenceLocale()))
        {
            case "hjmpts":
                return getHjmpTS();
            case "serieskey":
                return getSeriesKey();
            case "seriestype":
                return getSeriesType();
            case "currentvalue":
                return getCurrentValue();
        }
        throw new IllegalArgumentException("columnName");
    }


    public String toString()
    {
        return "NumberSeriesRow{currentValue=" + this.currentValue + ", hjmpTS=" + this.hjmpTS + ", seriesKey='" + this.seriesKey + "', seriesType=" + this.seriesType + "}";
    }
}

package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.NumberSeries;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;

public class DbNumberSeries implements NumberSeries
{
    private final String rowsHash;
    private final String seriesKey;
    private final long value;


    public DbNumberSeries(NumberSeriesRow numberSeriesRow)
    {
        Preconditions.checkNotNull(numberSeriesRow);
        Preconditions.checkNotNull(numberSeriesRow.getCurrentValue());
        Preconditions.checkNotNull(numberSeriesRow.getSeriesKey());
        this.rowsHash = HashGenerationStrategy.getForNumberseries().getHashFor((Row)numberSeriesRow);
        this.seriesKey = numberSeriesRow.getSeriesKey();
        this.value = numberSeriesRow.getCurrentValue().longValue();
    }


    public String getSeriesKey()
    {
        return this.seriesKey;
    }


    public long getValue()
    {
        return this.value;
    }


    public UniqueIdentifier getUniqueIdentifier()
    {
        return UniqueIdentifier.createFrom(this.seriesKey);
    }


    public String getHash()
    {
        return this.rowsHash;
    }
}

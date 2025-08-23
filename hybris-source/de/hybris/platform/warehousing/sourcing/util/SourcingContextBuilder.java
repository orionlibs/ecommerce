package de.hybris.platform.warehousing.sourcing.util;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import java.util.ArrayList;
import java.util.Collection;

public class SourcingContextBuilder
{
    private final Collection<AbstractOrderEntryModel> orderEntries = new ArrayList<>();
    private final Collection<SourcingLocation> sourcingLocations = new ArrayList<>();
    private SourcingResults sourcingResults;


    public SourcingContext build()
    {
        if(this.sourcingResults == null)
        {
            withDefaultResults();
        }
        SourcingContext sourcingContext = new SourcingContext();
        sourcingContext.setOrderEntries(getOrderEntries());
        sourcingContext.setResult(this.sourcingResults);
        sourcingContext.setSourcingLocations(this.sourcingLocations);
        return sourcingContext;
    }


    public static SourcingContextBuilder aSourcingContext()
    {
        return new SourcingContextBuilder();
    }


    public SourcingContextBuilder withSourcingLocation(SourcingLocation location)
    {
        this.sourcingLocations.add(location);
        return this;
    }


    public SourcingContextBuilder withOrderEntry(AbstractOrderEntryModel entry)
    {
        this.orderEntries.add(entry);
        return this;
    }


    public SourcingContextBuilder withDefaultResults()
    {
        SourcingResults results = new SourcingResults();
        results.setResults(Sets.newHashSet());
        this.sourcingResults = results;
        return this;
    }


    public SourcingContextBuilder withResults(SourcingResults results)
    {
        this.sourcingResults = results;
        return this;
    }


    protected Collection<SourcingLocation> getSourcingLocations()
    {
        return this.sourcingLocations;
    }


    protected Collection<AbstractOrderEntryModel> getOrderEntries()
    {
        return this.orderEntries;
    }


    protected SourcingResults getSourcingResults()
    {
        return this.sourcingResults;
    }


    public void setSourcingResults(SourcingResults sourcingResults)
    {
        this.sourcingResults = sourcingResults;
    }
}

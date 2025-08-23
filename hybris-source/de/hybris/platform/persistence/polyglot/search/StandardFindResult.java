package de.hybris.platform.persistence.polyglot.search;

import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.List;
import java.util.stream.Stream;

public class StandardFindResult implements FindResult
{
    public static final int NO_VALUE = -1;
    private final int count;
    private final int totalCount;
    private final Stream<ItemStateView> streamItemState;


    private StandardFindResult(int count, int totalCount, Stream<ItemStateView> streamItemState)
    {
        this.count = count;
        this.totalCount = totalCount;
        this.streamItemState = streamItemState;
    }


    public static StandardFindResultBuilder buildFromStream(Stream<ItemStateView> stream)
    {
        return (StandardFindResultBuilder)new StreamFindResultBuilder(stream);
    }


    public static StandardFindResultBuilder buildFromFindResults(List<FindResult> findResults)
    {
        return (StandardFindResultBuilder)new FindResultBuilder(findResults);
    }


    public int getCount()
    {
        return this.count;
    }


    public int getTotalCount()
    {
        return this.totalCount;
    }


    public Stream<ItemStateView> getResult()
    {
        return this.streamItemState;
    }
}

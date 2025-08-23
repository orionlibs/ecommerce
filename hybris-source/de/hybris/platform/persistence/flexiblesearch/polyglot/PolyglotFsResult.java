package de.hybris.platform.persistence.flexiblesearch.polyglot;

import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.stream.Stream;

public class PolyglotFsResult
{
    private final boolean isUnknown;
    private int totalCount;
    private final Stream<ItemStateView> result;


    private PolyglotFsResult(boolean isUnknown, Stream<ItemStateView> result)
    {
        this.isUnknown = isUnknown;
        this.result = result;
        this.totalCount = -1;
    }


    public boolean isUnknown()
    {
        return this.isUnknown;
    }


    public Stream<ItemStateView> getResult()
    {
        return this.result;
    }


    public static PolyglotFsResult unknown()
    {
        return new PolyglotFsResult(true, null);
    }


    public static PolyglotFsResult full(Stream<ItemStateView> result)
    {
        return new PolyglotFsResult(false, result);
    }


    public int getTotalCount()
    {
        return this.totalCount;
    }


    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }
}

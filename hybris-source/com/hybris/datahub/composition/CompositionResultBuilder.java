package com.hybris.datahub.composition;

import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.runtime.domain.CompositionActionStatusType;
import java.util.Collections;
import java.util.List;

public class CompositionResultBuilder
{
    private CompositionActionStatusType resultStatus = CompositionActionStatusType.SUCCESS;
    private List<CanonicalItem> canonicalItems = Collections.emptyList();
    private long canonicalItemsCount = 0L;
    private long processedRawItemsCount = 0L;
    private long ignoredRawItemsCount = 0L;
    private long successCount = 0L;
    private long archivedCount = 0L;
    private long deleteArchivedCount = 0L;
    private long errorCount = 0L;
    private long deletedCount = 0L;


    public static CompositionResultBuilder compositionResultBuilder()
    {
        return new CompositionResultBuilder();
    }


    public CompositionResultBuilder withStatus(CompositionActionStatusType status)
    {
        this.resultStatus = status;
        return this;
    }


    public CompositionResultBuilder withCanonicalItemsCount(int count)
    {
        return withCanonicalItemsCount(count);
    }


    public CompositionResultBuilder withCanonicalItemsCount(Long count)
    {
        return withCanonicalItemsCount((count != null) ? count.longValue() : 0L);
    }


    public CompositionResultBuilder withCanonicalItemsCount(long count)
    {
        this.canonicalItemsCount = count;
        return this;
    }


    public CompositionResultBuilder withCanonicalItems(List<CanonicalItem> items)
    {
        this.canonicalItems = items;
        return this;
    }


    public CompositionResultBuilder withProcessedRawItemsCount(long cnt)
    {
        this.processedRawItemsCount = cnt;
        return this;
    }


    public CompositionResultBuilder withIgnoredRawItemsCount(long cnt)
    {
        this.ignoredRawItemsCount = cnt;
        return this;
    }


    public CompositionResultBuilder withSuccessCount(long cnt)
    {
        this.successCount = cnt;
        return this;
    }


    public CompositionResultBuilder withArchivedCount(long cnt)
    {
        this.archivedCount = cnt;
        return this;
    }


    public CompositionResultBuilder withDeleteArchivedCount(long cnt)
    {
        this.deleteArchivedCount = cnt;
        return this;
    }


    public CompositionResultBuilder withErrorCount(long cnt)
    {
        this.errorCount = cnt;
        return this;
    }


    public CompositionResultBuilder withDeletedCount(long cnt)
    {
        this.deletedCount = cnt;
        return this;
    }


    public CompositionResult build()
    {
        return new CompositionResult(this.canonicalItems, this.resultStatus, this.canonicalItemsCount, this.processedRawItemsCount, this.ignoredRawItemsCount, this.successCount, this.archivedCount, this.deleteArchivedCount, this.errorCount, this.deletedCount);
    }
}

package com.hybris.datahub.composition;

import com.google.common.base.Preconditions;
import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.runtime.domain.CompositionActionStatusType;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class CompositionResult implements Serializable
{
    private static final long serialVersionUID = 2436925072051957165L;
    private final CompositionActionStatusType status;
    private final List<CanonicalItem> canonicalItems;
    private final long canonicalItemCount;
    private final long processedRawItemsCount;
    private final long ignoredRawItemsCount;
    private final long successCount;
    private final long archivedCount;
    private final long deleteArchivedCount;
    private final long errorCount;
    private final long deletedCount;


    public CompositionResult(CompositionActionStatusType status, int count)
    {
        this(status, Long.valueOf(count));
    }


    public CompositionResult(CompositionActionStatusType status, Long count)
    {
        this(Collections.emptyList(), status, count);
    }


    public CompositionResult(List<CanonicalItem> canonicalItems, CompositionActionStatusType status, Long count)
    {
        this(canonicalItems, status, (count != null) ? count.longValue() : 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }


    public CompositionResult(List<CanonicalItem> items, CompositionActionStatusType status, long canonicalItemCount, long processedCount, long ignoredCount, long successCount, long archivedCount, long deleteArchivedCount, long errorCount, long deletedCount)
    {
        Preconditions.checkArgument((items != null), "items cannot be null");
        Preconditions.checkArgument((status != null), "status cannot be null");
        Preconditions.checkArgument(status.isFinal(), "only final statuses can apply to composition result");
        this.canonicalItems = items;
        this.status = status;
        this.canonicalItemCount = canonicalItemCount;
        this.processedRawItemsCount = processedCount;
        this.ignoredRawItemsCount = ignoredCount;
        this.successCount = successCount;
        this.archivedCount = archivedCount;
        this.deleteArchivedCount = deleteArchivedCount;
        this.errorCount = errorCount;
        this.deletedCount = deletedCount;
    }


    public static CompositionResult success()
    {
        return (new CompositionResultBuilder()).withStatus(CompositionActionStatusType.SUCCESS).build();
    }


    public static CompositionResult failure()
    {
        return (new CompositionResultBuilder()).withStatus(CompositionActionStatusType.FAILURE).build();
    }


    public CompositionActionStatusType getStatus()
    {
        return this.status;
    }


    public List<CanonicalItem> getCanonicalItems()
    {
        return this.canonicalItems;
    }


    public long getCanonicalItemCount()
    {
        return this.canonicalItemCount;
    }


    public long getProcessedRawItemsCount()
    {
        return this.processedRawItemsCount;
    }


    public long getIgnoredRawItemsCount()
    {
        return this.ignoredRawItemsCount;
    }


    public long getSuccessCount()
    {
        return this.successCount;
    }


    public long getArchivedCount()
    {
        return this.archivedCount;
    }


    public long getDeleteArchivedCount()
    {
        return this.deleteArchivedCount;
    }


    public long getErrorCount()
    {
        return this.errorCount;
    }


    public long getDeletedCount()
    {
        return this.deletedCount;
    }


    public CompositionResult combineWith(CompositionResult another)
    {
        CompositionActionStatusType combinedStatus = worstOf(this.status, another.getStatus());
        return CompositionResultBuilder.compositionResultBuilder()
                        .withStatus(combinedStatus)
                        .withCanonicalItemsCount(this.canonicalItemCount + another.getCanonicalItemCount())
                        .withProcessedRawItemsCount(this.processedRawItemsCount + another.getProcessedRawItemsCount())
                        .withIgnoredRawItemsCount(this.ignoredRawItemsCount + another.getIgnoredRawItemsCount())
                        .withSuccessCount(this.successCount + another.getSuccessCount())
                        .withArchivedCount(this.archivedCount + another.getArchivedCount())
                        .withDeleteArchivedCount(this.deleteArchivedCount + another.getDeleteArchivedCount())
                        .withErrorCount(this.errorCount + another.getErrorCount())
                        .withDeletedCount(this.deletedCount + another.getDeletedCount())
                        .build();
    }


    private static CompositionActionStatusType worstOf(CompositionActionStatusType one, CompositionActionStatusType another)
    {
        if(one.equals(CompositionActionStatusType.FAILURE) || another.equals(CompositionActionStatusType.FAILURE))
        {
            return CompositionActionStatusType.FAILURE;
        }
        if(one.equals(CompositionActionStatusType.COMPLETE_W_ERRORS) || another.equals(CompositionActionStatusType.COMPLETE_W_ERRORS))
        {
            return CompositionActionStatusType.COMPLETE_W_ERRORS;
        }
        return CompositionActionStatusType.SUCCESS;
    }


    public boolean containsCounts()
    {
        return (this.processedRawItemsCount + this.ignoredRawItemsCount + this.successCount + this.archivedCount + this.deleteArchivedCount + this.errorCount + this.deletedCount > 0L);
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(this.status)
                        .append(this.canonicalItemCount)
                        .append(this.processedRawItemsCount)
                        .append(this.ignoredRawItemsCount)
                        .append(this.successCount)
                        .append(this.archivedCount)
                        .append(this.deleteArchivedCount)
                        .append(this.errorCount)
                        .append(this.deletedCount)
                        .toHashCode();
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof CompositionResult)
        {
            CompositionResult other = (CompositionResult)obj;
            return (new EqualsBuilder())
                            .append(this.status, other.status)
                            .append(this.canonicalItemCount, other.canonicalItemCount)
                            .append(this.processedRawItemsCount, other.processedRawItemsCount)
                            .append(this.ignoredRawItemsCount, other.ignoredRawItemsCount)
                            .append(this.successCount, other.successCount)
                            .append(this.archivedCount, other.archivedCount)
                            .append(this.deleteArchivedCount, other.deleteArchivedCount)
                            .append(this.errorCount, other.errorCount)
                            .append(this.deletedCount, other.deletedCount)
                            .isEquals();
        }
        return false;
    }


    public String toString()
    {
        return "CompositionResult{status=" + this.status + ", canonicalItemCount=" + this.canonicalItemCount + ", processedRawItemCount=" + this.processedRawItemsCount + ", ignoredRawItemCount=" + this.ignoredRawItemsCount + ", successCount=" + this.successCount + ", archivedCount="
                        + this.archivedCount + ", deleteArchivedCount=" + this.deleteArchivedCount + ", errorCount=" + this.errorCount + ", deletedCount=" + this.deletedCount + ", items=" + this.canonicalItems + "}";
    }
}

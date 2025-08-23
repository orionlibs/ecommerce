package com.hybris.datahub.api.publication;

public class PublicationResultBuilder
{
    private long publicationId;
    private String completionStatus;
    private long successCount = 0L;
    private long internalErrorCount = 0L;
    private long externalErrorCount = 0L;
    private long ignoredCount = 0L;


    public static PublicationResultBuilder publicationResultBuilder()
    {
        return new PublicationResultBuilder();
    }


    public PublicationResultBuilder withPublicationId(long publicationId)
    {
        this.publicationId = publicationId;
        return this;
    }


    public PublicationResultBuilder withCompletionStatus(String completionStatus)
    {
        this.completionStatus = completionStatus;
        return this;
    }


    public PublicationResultBuilder withSuccessCount(long successCount)
    {
        this.successCount = successCount;
        return this;
    }


    public PublicationResultBuilder withInternalErrorCount(long internalErrorCount)
    {
        this.internalErrorCount = internalErrorCount;
        return this;
    }


    public PublicationResultBuilder withExternalErrorCount(long externalErrorCount)
    {
        this.externalErrorCount = externalErrorCount;
        return this;
    }


    public PublicationResultBuilder withIgnoredCount(long ignoredCount)
    {
        this.ignoredCount = ignoredCount;
        return this;
    }


    public PublicationResult build()
    {
        return new PublicationResult(this.publicationId, this.completionStatus, this.successCount, this.internalErrorCount, this.externalErrorCount, this.ignoredCount);
    }
}

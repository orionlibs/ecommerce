package de.hybris.platform.retention;

import de.hybris.platform.processing.model.AbstractRetentionRuleModel;

public class RetentionRequestParams
{
    private final AbstractRetentionRuleModel retentionRule;
    private final Integer batchSize;


    public AbstractRetentionRuleModel getRetentionRule()
    {
        return this.retentionRule;
    }


    public Integer getBatchSize()
    {
        return this.batchSize;
    }


    private RetentionRequestParams(Builder builder)
    {
        this.retentionRule = builder.retentionRule;
        this.batchSize = builder.batchSize;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}

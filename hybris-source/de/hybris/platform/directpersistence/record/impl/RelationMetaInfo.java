package de.hybris.platform.directpersistence.record.impl;

import com.google.common.base.Preconditions;

public class RelationMetaInfo
{
    private final String relationName;
    private final String targetTypeCode;
    private final String sourceTypeCode;
    private final String foreignKeyOnTarget;
    private final boolean oneToMany;
    private final boolean sourceOrdered;
    private final boolean targetOrdered;
    private final boolean targetPartOf;
    private final boolean sourcePartOf;


    private RelationMetaInfo(Builder builder)
    {
        Preconditions.checkArgument((builder.relationName != null), "relationName is required");
        this.relationName = builder.relationName;
        this.targetTypeCode = builder.targetTypeCode;
        this.sourceTypeCode = builder.sourceTypeCode;
        this.foreignKeyOnTarget = builder.foreignKeyOnTarget;
        this.oneToMany = builder.oneToMany;
        this.sourceOrdered = builder.sourceOrdered;
        this.targetOrdered = builder.targetOrdered;
        this.sourcePartOf = builder.sourcePartOf;
        this.targetPartOf = builder.targetPartOf;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public String getRelationName()
    {
        return this.relationName;
    }


    public boolean isSourceOrdered()
    {
        return this.sourceOrdered;
    }


    public boolean isTargetOrdered()
    {
        return this.targetOrdered;
    }


    public boolean isOneToMany()
    {
        return this.oneToMany;
    }


    public String getTargetTypeCode()
    {
        return this.targetTypeCode;
    }


    public String getSourceTypeCode()
    {
        return this.sourceTypeCode;
    }


    public String getForeignKeyOnTarget()
    {
        return this.foreignKeyOnTarget;
    }


    public boolean isTargetPartOf()
    {
        return this.targetPartOf;
    }


    public boolean isSourcePartOf()
    {
        return this.sourcePartOf;
    }
}

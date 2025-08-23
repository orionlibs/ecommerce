package com.hybris.datahub.grouping;

import com.hybris.datahub.domain.TargetItemMetadata;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import javax.annotation.concurrent.Immutable;

@Immutable
public class TargetItemCreationContext
{
    private final TargetSystemPublication targetSystemPublication;
    private final TargetItemMetadata targetItemType;


    public TargetItemCreationContext(TargetSystemPublication publication, TargetItemMetadata metadata)
    {
        this.targetSystemPublication = publication;
        this.targetItemType = metadata;
    }


    public TargetSystemPublication getTargetSystemPublication()
    {
        return this.targetSystemPublication;
    }


    public TargetItemMetadata getTargetItemMetadata()
    {
        return this.targetItemType;
    }


    public String getTargetItemTypeCode()
    {
        return (this.targetItemType != null) ? this.targetItemType.getItemType() : "";
    }
}

package com.hybris.datahub.validation;

import com.hybris.datahub.domain.TargetItemMetadata;
import com.hybris.datahub.dto.metadata.ItemTypeData;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CircularTypeDependencyFailure extends DependencyValidationFailure
{
    public CircularTypeDependencyFailure(ItemTypeData type, TargetItemMetadata dep)
    {
        this(type, dep.getItemType());
    }


    public CircularTypeDependencyFailure(ItemTypeData type, String dep)
    {
        super(type, dep, "Types " + type + " and type " + dep + " make a circular dependency");
    }
}

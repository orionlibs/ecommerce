package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.ItemTypeData;
import javax.annotation.concurrent.Immutable;

@Immutable
public class MissingTypeDependencyFailure extends DependencyValidationFailure
{
    public MissingTypeDependencyFailure(ItemTypeData type, String dep)
    {
        super(type, dep, "Type " + dep + " declared in dependencies of type " + type + " does not exist");
    }
}

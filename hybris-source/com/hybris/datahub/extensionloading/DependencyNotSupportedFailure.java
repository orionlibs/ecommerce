package com.hybris.datahub.extensionloading;

import com.hybris.datahub.validation.ValidationFailure;
import com.hybris.datahub.validation.ValidationFailureType;

public class DependencyNotSupportedFailure extends ValidationFailure
{
    protected static final String DYNAMICALLY_LOADED_EXTENSION_CANNOT_CONTAIN_DEPENDENCIES = "Dynamically loaded extension cannot contain dependencies.";


    public DependencyNotSupportedFailure()
    {
        super("Dynamically loaded extension cannot contain dependencies.", ValidationFailureType.FATAL);
    }
}

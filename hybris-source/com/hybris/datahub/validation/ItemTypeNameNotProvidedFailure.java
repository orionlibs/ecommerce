package com.hybris.datahub.validation;

public class ItemTypeNameNotProvidedFailure extends ValidationFailure
{
    public ItemTypeNameNotProvidedFailure(Object type)
    {
        super("Type name cannot be blank or null in " + type, ValidationFailureType.FATAL);
    }
}

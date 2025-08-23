package com.hybris.datahub.validation;

public class PrimaryKeyRequiredFailure extends ValidationFailure
{
    public PrimaryKeyRequiredFailure(String type)
    {
        super(message(type), ValidationFailureType.FATAL);
    }


    private static String message(String type)
    {
        return String.format("No primary key is set for canonical item %s", new Object[] {type});
    }
}

package de.hybris.bootstrap.ddl.sql;

public interface ExtendedAwareIndex
{
    void setExtendedParams(ExtendedParamsForIndex paramExtendedParamsForIndex);


    ExtendedParamsForIndex getExtendedParams();
}

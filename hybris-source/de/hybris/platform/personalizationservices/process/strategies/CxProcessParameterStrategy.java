package de.hybris.platform.personalizationservices.process.strategies;

import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;

public interface CxProcessParameterStrategy
{
    void load(CxPersonalizationProcessModel paramCxPersonalizationProcessModel);


    void store(CxPersonalizationProcessModel paramCxPersonalizationProcessModel);


    boolean supports(CxProcessParameterType paramCxProcessParameterType);
}

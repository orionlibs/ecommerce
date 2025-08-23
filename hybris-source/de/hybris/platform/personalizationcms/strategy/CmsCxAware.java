package de.hybris.platform.personalizationcms.strategy;

public interface CmsCxAware
{
    String getCxContainerUid();


    String getCxContainerType();


    String getCxActionCode();


    String getCxCustomizationCode();


    String getCxVariationCode();


    String getContainerSourceId();
}

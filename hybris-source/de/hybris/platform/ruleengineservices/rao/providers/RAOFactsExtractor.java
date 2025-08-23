package de.hybris.platform.ruleengineservices.rao.providers;

import java.util.Set;

public interface RAOFactsExtractor
{
    Set expandFact(Object paramObject);


    String getTriggeringOption();


    boolean isMinOption();


    boolean isDefault();
}

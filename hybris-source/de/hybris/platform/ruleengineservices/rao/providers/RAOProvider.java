package de.hybris.platform.ruleengineservices.rao.providers;

import java.util.Set;

public interface RAOProvider<T>
{
    Set expandFactModel(T paramT);
}

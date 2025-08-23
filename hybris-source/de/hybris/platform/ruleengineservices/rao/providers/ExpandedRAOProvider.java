package de.hybris.platform.ruleengineservices.rao.providers;

import java.util.Collection;
import java.util.Set;

public interface ExpandedRAOProvider<T> extends RAOProvider<T>
{
    Set expandFactModel(T paramT, Collection<String> paramCollection);
}

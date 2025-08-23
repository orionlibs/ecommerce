package de.hybris.platform.ruleengineservices.rao.providers;

import de.hybris.platform.ruleengineservices.enums.FactContextType;
import de.hybris.platform.ruleengineservices.rao.providers.impl.FactContext;
import java.util.Collection;

public interface FactContextFactory
{
    FactContext createFactContext(FactContextType paramFactContextType, Collection<?> paramCollection);
}

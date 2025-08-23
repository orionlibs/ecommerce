package de.hybris.platform.cockpit.wizards.generic.strategies;

import de.hybris.platform.cockpit.session.impl.CreateContext;
import java.util.Map;

public interface PredefinedValuesStrategy
{
    Map<String, Object> getPredefinedValues(CreateContext paramCreateContext);
}

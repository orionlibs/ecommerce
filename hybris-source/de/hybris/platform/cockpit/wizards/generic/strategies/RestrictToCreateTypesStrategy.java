package de.hybris.platform.cockpit.wizards.generic.strategies;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import java.util.Set;

public interface RestrictToCreateTypesStrategy
{
    Set<ObjectType> getAllowedTypes();
}

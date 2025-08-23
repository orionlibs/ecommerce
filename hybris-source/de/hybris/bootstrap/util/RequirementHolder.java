package de.hybris.bootstrap.util;

import java.util.Set;

public interface RequirementHolder
{
    Set<? extends RequirementHolder> getRequirements();
}

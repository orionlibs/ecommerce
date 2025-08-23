package de.hybris.platform.personalizationservices.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CxDaoStrategySelector
{
    Optional<CxDaoStrategy> selectStrategy(Collection<? extends CxDaoStrategy> paramCollection, Map<String, String> paramMap);
}

package de.hybris.platform.ruleengine.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import java.util.List;

public interface CatalogVersionFinderStrategy
{
    List<CatalogVersionModel> findCatalogVersionsByRulesModule(AbstractRulesModuleModel paramAbstractRulesModuleModel);
}

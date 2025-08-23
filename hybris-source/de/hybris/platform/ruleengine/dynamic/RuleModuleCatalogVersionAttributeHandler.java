package de.hybris.platform.ruleengine.dynamic;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.strategies.CatalogVersionFinderStrategy;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class RuleModuleCatalogVersionAttributeHandler implements DynamicAttributeHandler<Collection<CatalogVersionModel>, AbstractRulesModuleModel>
{
    private CatalogVersionFinderStrategy catalogVersionFinderStrategy;


    public Collection<CatalogVersionModel> get(AbstractRulesModuleModel rulesModule)
    {
        return getCatalogVersionFinderStrategy().findCatalogVersionsByRulesModule(rulesModule);
    }


    public void set(AbstractRulesModuleModel rulesModule, Collection<CatalogVersionModel> catalogVersions)
    {
        throw new UnsupportedOperationException("AbstractRulesModuleModel.catalogVersions is readonly attribute");
    }


    protected CatalogVersionFinderStrategy getCatalogVersionFinderStrategy()
    {
        return this.catalogVersionFinderStrategy;
    }


    @Required
    public void setCatalogVersionFinderStrategy(CatalogVersionFinderStrategy catalogVersionFinderStrategy)
    {
        this.catalogVersionFinderStrategy = catalogVersionFinderStrategy;
    }
}

package de.hybris.platform.personalizationfacades.action.converters.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.ActionFullData;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import org.springframework.util.Assert;

public class ActionFullPopulator implements Populator<CxAbstractActionModel, ActionFullData>
{
    public void populate(CxAbstractActionModel source, ActionFullData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setActionCode(source.getCode());
        target.setActionRank(source.getRank());
        if(source.getCatalogVersion() != null)
        {
            CatalogVersionModel cv = source.getCatalogVersion();
            target.setActionCatalogVersion(cv.getVersion());
            if(cv.getCatalog() != null)
            {
                target.setActionCatalog(cv.getCatalog().getId());
            }
        }
        if(source.getVariation() != null)
        {
            CxVariationModel variation = source.getVariation();
            target.setVariationCode(variation.getCode());
            target.setVariationName(variation.getName());
            target.setVariationRank(variation.getRank());
            target.setVariationStatus(variation.getStatus().name());
            if(variation.getCustomization() != null)
            {
                CxCustomizationModel customization = variation.getCustomization();
                target.setCustomizationCode(customization.getCode());
                target.setCustomizationName(customization.getName());
                target.setCustomizationRank(customization.getRank());
                target.setCustomizationStatus(customization.getStatus().name());
            }
        }
    }
}

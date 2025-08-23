package de.hybris.platform.personalizationfacades.customization.converters.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.enums.ItemStatus;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import org.springframework.util.Assert;

public class CustomizationPopulator implements Populator<CxCustomizationModel, CustomizationData>
{
    public void populate(CxCustomizationModel source, CustomizationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setRank(source.getRank());
        target.setActive(Boolean.valueOf(source.isActive()));
        target.setStatus(getItemStatus(source.getStatus()));
        target.setDescription(source.getLongDescription());
        target.setEnabledStartDate(source.getEnabledStartDate());
        target.setEnabledEndDate(source.getEnabledEndDate());
        if(source.getCatalogVersion() != null)
        {
            CatalogVersionModel cv = source.getCatalogVersion();
            target.setCatalogVersion(cv.getVersion());
            if(cv.getCatalog() != null)
            {
                target.setCatalog(cv.getCatalog().getId());
            }
        }
    }


    protected ItemStatus getItemStatus(CxItemStatus status)
    {
        if(status == null)
        {
            return ItemStatus.ENABLED;
        }
        return ItemStatus.valueOf(status.name());
    }
}

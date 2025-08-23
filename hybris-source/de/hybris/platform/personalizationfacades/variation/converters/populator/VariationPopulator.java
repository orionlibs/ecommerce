package de.hybris.platform.personalizationfacades.variation.converters.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.ItemStatus;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import org.springframework.util.Assert;

public class VariationPopulator implements Populator<CxVariationModel, VariationData>
{
    public void populate(CxVariationModel source, VariationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setActive(Boolean.valueOf(source.isActive()));
        target.setStatus(getItemStatus(source));
        target.setEnabled(Boolean.valueOf((target.getStatus() == ItemStatus.ENABLED)));
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setRank(source.getRank());
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


    protected ItemStatus getItemStatus(CxVariationModel source)
    {
        if(source.getStatus() == null)
        {
            return source.isEnabled() ? ItemStatus.ENABLED : ItemStatus.DISABLED;
        }
        return ItemStatus.valueOf(source.getStatus().name());
    }
}

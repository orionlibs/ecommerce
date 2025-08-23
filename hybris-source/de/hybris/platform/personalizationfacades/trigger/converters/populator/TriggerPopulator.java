package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import org.springframework.util.Assert;

public class TriggerPopulator implements Populator<CxAbstractTriggerModel, TriggerData>
{
    public void populate(CxAbstractTriggerModel source, TriggerData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCode(source.getCode());
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
}

package de.hybris.platform.personalizationfacades.action.converters.populator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import org.springframework.util.Assert;

public class ActionPopulator implements Populator<CxAbstractActionModel, ActionData>
{
    public void populate(CxAbstractActionModel source, ActionData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCode(source.getCode());
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
}

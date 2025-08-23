package de.hybris.platform.personalizationcmsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationcmsweb.data.CxCmsActionFullData;
import org.springframework.util.Assert;

public class CxCmsActionFullPopulator implements Populator<CxCmsActionModel, CxCmsActionFullData>
{
    public void populate(CxCmsActionModel source, CxCmsActionFullData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setComponentId(source.getComponentId());
        target.setComponentCatalog(source.getComponentCatalog());
        target.setContainerId(source.getContainerId());
    }
}

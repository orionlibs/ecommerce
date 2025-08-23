package de.hybris.platform.personalizationcmsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationcmsweb.data.CxCmsActionData;
import org.springframework.util.Assert;

public class CxCmsActionPopulator implements Populator<CxCmsActionModel, CxCmsActionData>
{
    public void populate(CxCmsActionModel source, CxCmsActionData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setComponentId(source.getComponentId());
        target.setComponentCatalog(source.getComponentCatalog());
        target.setContainerId(source.getContainerId());
    }
}

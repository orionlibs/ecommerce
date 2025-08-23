package de.hybris.platform.personalizationcmsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationcmsweb.data.CxCmsActionData;
import org.springframework.util.Assert;

public class CxCmsActionReversePopulator implements Populator<CxCmsActionData, CxCmsActionModel>
{
    public void populate(CxCmsActionData source, CxCmsActionModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setComponentId(source.getComponentId());
        target.setContainerId(source.getContainerId());
        target.setComponentCatalog(source.getComponentCatalog());
    }
}

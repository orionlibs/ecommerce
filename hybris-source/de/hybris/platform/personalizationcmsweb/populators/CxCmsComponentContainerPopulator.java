package de.hybris.platform.personalizationcmsweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationcmsweb.data.CxCmsComponentContainerData;
import org.springframework.util.Assert;

public class CxCmsComponentContainerPopulator implements Populator<CxCmsComponentContainerModel, CxCmsComponentContainerData>
{
    public void populate(CxCmsComponentContainerModel source, CxCmsComponentContainerData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setUid(source.getUid());
        target.setSourceId(source.getSourceId());
        if(source.getDefaultCmsComponent() != null)
        {
            target.setDefaultComponentUid(source.getDefaultCmsComponent().getUid());
        }
    }
}

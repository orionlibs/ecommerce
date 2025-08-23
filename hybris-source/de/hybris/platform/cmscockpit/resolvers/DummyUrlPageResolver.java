package de.hybris.platform.cmscockpit.resolvers;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.resolvers.CMSUrlResolver;

public class DummyUrlPageResolver implements CMSUrlResolver<AbstractPageModel>
{
    public String resolve(AbstractPageModel source)
    {
        return source.getUid();
    }
}

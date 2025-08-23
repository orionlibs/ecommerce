package de.hybris.platform.personalizationsearchweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationsearch.model.CxSearchProfileActionModel;
import de.hybris.platform.personalizationsearchweb.data.CxSearchProfileActionData;

public class CxSearchProfileActionPopulator implements Populator<CxSearchProfileActionModel, CxSearchProfileActionData>
{
    public void populate(CxSearchProfileActionModel source, CxSearchProfileActionData target)
    {
        target.setSearchProfileCode(source.getSearchProfileCode());
        target.setSearchProfileCatalog(source.getSearchProfileCatalog());
    }
}

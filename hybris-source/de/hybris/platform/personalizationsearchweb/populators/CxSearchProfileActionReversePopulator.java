package de.hybris.platform.personalizationsearchweb.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationsearch.model.CxSearchProfileActionModel;
import de.hybris.platform.personalizationsearchweb.data.CxSearchProfileActionData;

public class CxSearchProfileActionReversePopulator implements Populator<CxSearchProfileActionData, CxSearchProfileActionModel>
{
    public void populate(CxSearchProfileActionData source, CxSearchProfileActionModel target)
    {
        target.setSearchProfileCode(source.getSearchProfileCode());
        target.setSearchProfileCatalog(source.getSearchProfileCatalog());
    }
}

package de.hybris.platform.adaptivesearchfacades.converters.populators;

import de.hybris.adaptivesearchfacades.data.AsSearchProfileData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.converters.Populator;

public class AsSearchProfileDataPopulator implements Populator<AbstractAsSearchProfileModel, AsSearchProfileData>
{
    public void populate(AbstractAsSearchProfileModel source, AsSearchProfileData target)
    {
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setIndexType(source.getIndexType());
        target.setCatalogVersion(source.getCatalogVersion().getCatalog().getId() + ":" + source.getCatalogVersion().getCatalog().getId());
    }
}

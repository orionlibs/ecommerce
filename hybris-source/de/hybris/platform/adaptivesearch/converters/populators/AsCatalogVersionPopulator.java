package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.data.AsCatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;

public class AsCatalogVersionPopulator implements Populator<CatalogVersionModel, AsCatalogVersion>
{
    public void populate(CatalogVersionModel source, AsCatalogVersion target)
    {
        target.setCatalogId(source.getCatalog().getId());
        target.setVersion(source.getVersion());
    }
}

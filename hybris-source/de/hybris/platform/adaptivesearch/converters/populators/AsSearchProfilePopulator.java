package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.data.AbstractAsSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsCatalogVersion;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;

public class AsSearchProfilePopulator implements Populator<AbstractAsSearchProfileModel, AbstractAsSearchProfile>
{
    private Converter<CatalogVersionModel, AsCatalogVersion> asCatalogVersionConverter;


    public void populate(AbstractAsSearchProfileModel source, AbstractAsSearchProfile target)
    {
        if(source.getCatalogVersion() != null)
        {
            target.setCatalogVersion((AsCatalogVersion)this.asCatalogVersionConverter.convert(source.getCatalogVersion()));
        }
        target.setCode(source.getCode());
        target.setIndexType(source.getIndexType());
        target.setQueryContext(source.getQueryContext());
    }


    public Converter<CatalogVersionModel, AsCatalogVersion> getAsCatalogVersionConverter()
    {
        return this.asCatalogVersionConverter;
    }


    @Required
    public void setAsCatalogVersionConverter(Converter<CatalogVersionModel, AsCatalogVersion> asCatalogVersionConverter)
    {
        this.asCatalogVersionConverter = asCatalogVersionConverter;
    }
}

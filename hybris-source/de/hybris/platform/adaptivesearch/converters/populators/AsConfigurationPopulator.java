package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsConfiguration;
import de.hybris.platform.adaptivesearch.data.AsCatalogVersion;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;

public class AsConfigurationPopulator implements ContextAwarePopulator<AbstractAsConfigurationModel, AbstractAsConfiguration, AsConfigurationConverterContext>
{
    private Converter<CatalogVersionModel, AsCatalogVersion> asCatalogVersionConverter;


    public void populate(AbstractAsConfigurationModel source, AbstractAsConfiguration target, AsConfigurationConverterContext context)
    {
        if(source.getCatalogVersion() != null)
        {
            target.setCatalogVersion((AsCatalogVersion)this.asCatalogVersionConverter.convert(source.getCatalogVersion()));
        }
        target.setUid(source.getUid());
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

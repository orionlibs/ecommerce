package de.hybris.platform.solrfacetsearch.config.mapping.converters;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.mapping.FacetSearchConfigMapping;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Required;

@FacetSearchConfigMapping
public class ItemModelConverter extends CustomConverter<ItemModel, ItemModel>
{
    private ModelService modelService;


    public boolean canConvert(Type<?> sourceType, Type<?> destinationType)
    {
        return (this.sourceType.isAssignableFrom(sourceType) && this.destinationType.isAssignableFrom(destinationType));
    }


    public ItemModel convert(ItemModel source, Type<? extends ItemModel> destinationType, MappingContext mappingContext)
    {
        return (ItemModel)this.modelService.get(source.getPk());
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public boolean equals(Object other)
    {
        return super.equals(other);
    }


    public int hashCode()
    {
        return super.hashCode();
    }
}

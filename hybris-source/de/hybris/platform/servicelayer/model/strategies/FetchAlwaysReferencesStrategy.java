package de.hybris.platform.servicelayer.model.strategies;

import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;

public class FetchAlwaysReferencesStrategy implements FetchStrategy
{
    private final ItemModelConverter itemModelConverter;


    public FetchAlwaysReferencesStrategy(ModelConverter modelConverter)
    {
        if(modelConverter instanceof ItemModelConverter)
        {
            this.itemModelConverter = (ItemModelConverter)modelConverter;
        }
        else
        {
            this.itemModelConverter = null;
        }
    }


    public boolean needsFetch(String attributeQualifier)
    {
        if(this.itemModelConverter == null)
        {
            return false;
        }
        return this.itemModelConverter.getInfo(attributeQualifier).getAttributeInfo().isReference();
    }


    public boolean isMutable()
    {
        return false;
    }
}

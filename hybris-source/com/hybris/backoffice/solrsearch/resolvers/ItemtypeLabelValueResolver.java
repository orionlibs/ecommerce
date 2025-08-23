package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

public class ItemtypeLabelValueResolver extends ItemModelLabelValueResolver
{
    private TypeService typeService;


    protected ItemModel provideModel(ItemModel model)
    {
        String itemtypeCode = model.getItemtype();
        return (ItemModel)getTypeService().getComposedTypeForCode(itemtypeCode);
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}

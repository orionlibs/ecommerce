package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.ModelCloningStrategy;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemModelCloneStrategy implements ModelCloningStrategy
{
    private ItemModelCloneCreator itemModelCloneCreator;
    private TypeService typeService;


    public <T> T clone(T original, ModelCloningContext ctx)
    {
        return (T)this.itemModelCloneCreator.copy((ItemModel)original, ctx);
    }


    public <T> T clone(Object original, String targetType, ModelCloningContext ctx)
    {
        if(targetType == null)
        {
            return clone((T)original, ctx);
        }
        return (T)this.itemModelCloneCreator.copy(this.typeService.getComposedTypeForCode(targetType), (ItemModel)original, ctx);
    }


    @Required
    public void setItemModelCloneCreator(ItemModelCloneCreator itemModelCloneCreator)
    {
        this.itemModelCloneCreator = itemModelCloneCreator;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}

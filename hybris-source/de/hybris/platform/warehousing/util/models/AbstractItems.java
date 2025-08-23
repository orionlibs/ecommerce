package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collection;
import java.util.function.Supplier;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractItems<T extends ItemModel>
{
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;


    protected T getFromCollectionOrSaveAndReturn(Supplier<Collection<T>> getter, Supplier<T> creator)
    {
        ItemModel itemModel;
        T model = null;
        if(!CollectionUtils.isEmpty(getter.get()))
        {
            itemModel = ((Collection<ItemModel>)getter.get()).iterator().next();
        }
        else
        {
            itemModel = (ItemModel)creator.get();
            getModelService().save(itemModel);
        }
        return (T)itemModel;
    }


    protected T getOrSaveAndReturn(Supplier<T> getter, Supplier<T> creator)
    {
        ItemModel itemModel;
        T model = null;
        try
        {
            itemModel = (ItemModel)getter.get();
            if(itemModel == null)
            {
                throw new ModelNotFoundException("DAO returned null.");
            }
        }
        catch(ModelNotFoundException | de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException e)
        {
            itemModel = (ItemModel)creator.get();
            getModelService().save(itemModel);
        }
        return (T)itemModel;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}

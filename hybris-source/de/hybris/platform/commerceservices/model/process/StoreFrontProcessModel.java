package de.hybris.platform.commerceservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;

public class StoreFrontProcessModel extends BusinessProcessModel
{
    public static final String _TYPECODE = "StoreFrontProcess";
    public static final String SITE = "site";
    public static final String STORE = "store";


    public StoreFrontProcessModel()
    {
    }


    public StoreFrontProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoreFrontProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoreFrontProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "site", type = Accessor.Type.GETTER)
    public BaseSiteModel getSite()
    {
        return (BaseSiteModel)getPersistenceContext().getPropertyValue("site");
    }


    @Accessor(qualifier = "store", type = Accessor.Type.GETTER)
    public BaseStoreModel getStore()
    {
        return (BaseStoreModel)getPersistenceContext().getPropertyValue("store");
    }


    @Accessor(qualifier = "site", type = Accessor.Type.SETTER)
    public void setSite(BaseSiteModel value)
    {
        getPersistenceContext().setPropertyValue("site", value);
    }


    @Accessor(qualifier = "store", type = Accessor.Type.SETTER)
    public void setStore(BaseStoreModel value)
    {
        getPersistenceContext().setPropertyValue("store", value);
    }
}

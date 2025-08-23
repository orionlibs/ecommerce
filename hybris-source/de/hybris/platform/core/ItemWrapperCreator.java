package de.hybris.platform.core;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import org.apache.log4j.Logger;

public interface ItemWrapperCreator
{
    Item.ItemImpl create(Tenant paramTenant, ItemRemote paramItemRemote);


    default ItemImplCreationResult create(Tenant tenant, PK pk)
    {
        try
        {
            ItemRemote remItem = tenant.getSystemEJB().findRemoteObjectByPK(pk);
            Item.ItemImpl impl = create(tenant, remItem);
            return ItemImplCreationResult.existing(impl);
        }
        catch(EJBItemNotFoundException | de.hybris.platform.util.jeeapi.YNoSuchEntityException e)
        {
            Logger.getLogger(ItemWrapperCreator.class).debug(e);
            return ItemImplCreationResult.missing(pk);
        }
        catch(EJBInvalidParameterException e)
        {
            return ItemImplCreationResult.failed(pk, (Exception)e);
        }
    }
}

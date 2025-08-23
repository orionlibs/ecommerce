package de.hybris.platform.store.services.impl;

import de.hybris.platform.basecommerce.strategies.BaseStoreSelectorStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBaseStoreService implements BaseStoreService
{
    private BaseStoreDao baseStoreDao;
    private List<BaseStoreSelectorStrategy> baseStoreSelectorStrategies;


    public List<BaseStoreModel> getAllBaseStores()
    {
        return this.baseStoreDao.findAllBaseStores();
    }


    public BaseStoreModel getBaseStoreForUid(String uid) throws AmbiguousIdentifierException, UnknownIdentifierException
    {
        List<BaseStoreModel> result = this.baseStoreDao.findBaseStoresByUid(uid);
        if(result.isEmpty())
        {
            throw new UnknownIdentifierException("Base store with uid '" + uid + "' not found!");
        }
        if(result.size() > 1)
        {
            throw new AmbiguousIdentifierException("Base store uid '" + uid + "' is not unique, " + result.size() + " base stores found!");
        }
        return result.get(0);
    }


    public BaseStoreModel getCurrentBaseStore()
    {
        BaseStoreModel result = null;
        if(!CollectionUtils.isEmpty(this.baseStoreSelectorStrategies))
        {
            for(BaseStoreSelectorStrategy strategy : this.baseStoreSelectorStrategies)
            {
                result = strategy.getCurrentBaseStore();
                if(result != null)
                {
                    break;
                }
            }
        }
        return result;
    }


    @Required
    public void setBaseStoreDao(BaseStoreDao baseStoreDao)
    {
        this.baseStoreDao = baseStoreDao;
    }


    @Required
    public void setBaseStoreSelectorStrategies(List<BaseStoreSelectorStrategy> baseStoreSelectorStrategies)
    {
        this.baseStoreSelectorStrategies = baseStoreSelectorStrategies;
    }
}

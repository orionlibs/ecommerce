package de.hybris.platform.store.daos;

import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

public interface BaseStoreDao
{
    List<BaseStoreModel> findBaseStoresByUid(String paramString);


    List<BaseStoreModel> findAllBaseStores();
}
